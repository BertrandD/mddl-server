package com.gameserver.controllers.instances;

import com.auth.Account;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.manager.BuildingTaskManager;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.ModulableBuilding;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.tasks.BuildingTask;
import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingService;
import com.gameserver.services.BuildingTaskService;
import com.gameserver.services.InventoryService;
import com.gameserver.services.PlayerService;
import com.gameserver.services.ValidatorService;
import com.util.response.JsonResponse;
import com.util.response.JsonResponseType;
import com.util.response.SystemMessageId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/building", produces = "application/json")
public class BuildingInstanceController {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingTaskService buildingTaskService;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validator;

    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse findAll(@AuthenticationPrincipal Account pAccount) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);
        return new JsonResponse(base.getBuildings());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findOne(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        final BuildingInstance building = buildingService.findBy(player.getCurrentBase(), id);
        if(building == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BUILDING_NOT_FOUND);
        building.setLang(pAccount.getLang());
        return new JsonResponse(building);
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "building") String templateId) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);
        base.initializeStats();

        final BuildingInstance hasBuilding = buildingService.findByBaseAndBuildingId(base, templateId);
        if(hasBuilding != null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BUILDING_ALREADY_EXIST);

        if(BuildingData.getInstance().getBuilding(templateId) == null) return new JsonResponse(JsonResponseType.ERROR, "Building " + templateId + " does not exist.");

        final BuildingInstance tempBuilding = new BuildingInstance(base, templateId);

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        final JsonResponse validate = validator.validateBuildingRequirements(base, tempBuilding, collector, pAccount.getLang());
        if(validate != null) return validate;

        final BuildingInstance building = buildingService.create(base, templateId);
        if(building == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BUILDING_CANNOT_CREATE);

        collector.forEach(inventoryService::consumeItem);

        base.addBuilding(building, base.getBuildingPositions().size()+1); // temp position disable
        baseService.update(base);

        base.initializeStats();
        buildingTaskManager.ScheduleUpgrade(building);

        JsonResponse response = new JsonResponse(building);
        response.addMeta("base", base);
        return response;
    }

    @RequestMapping(value = "/{id}/upgrade", method = RequestMethod.POST)
    public JsonResponse upgrade(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        final Base base = player.getCurrentBase();

        final BuildingInstance building = base.getBuildings().stream().filter(k->k.getId().equals(id)).findFirst().orElse(null);
        if(building == null){
            return new JsonResponse(pAccount.getLang(), SystemMessageId.BUILDING_NOT_FOUND);
        }

        final BuildingTask lastInQueue = buildingTaskService.findFirstByBuildingOrderByEndsAtDesc(building.getId());
        final Building template = building.getTemplate();
        if(building.getCurrentLevel() >= template.getMaxLevel() ||
            (lastInQueue != null && lastInQueue.getLevel() + 1 >= template.getMaxLevel())){
            return new JsonResponse(pAccount.getLang(), SystemMessageId.BUILDING_MAX_LEVEL_REACHED);
        }

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        final JsonResponse validate = validator.validateBuildingRequirements(base, building, collector, pAccount.getLang());
        if(validate != null) return validate;

        collector.forEach(inventoryService::consumeItem);

        base.initializeStats();
        buildingTaskManager.ScheduleUpgrade(building);
        final List<BuildingTask> tasks = buildingTaskService.findByBuildingOrderByEndsAtAsc(building.getId());

        JsonResponse response = new JsonResponse(building);
        response.addMeta("queue", tasks);
        response.addMeta("base", base);
        return response;
    }

    @RequestMapping(value = "/{id}/attach/module/{module}")
    public JsonResponse attachModule(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String buildingInstId, @PathVariable("module") String moduleId) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.BASE_NOT_FOUND);

        BuildingInstance building = base.getBuildings().stream().filter(k->k.getId().equals(buildingInstId)).findFirst().orElse(null);
        if(building == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.BUILDING_NOT_FOUND);

        final ItemInstance module = base.getBaseInventory().getItems().stream().filter(k -> k.getTemplateId().equals(moduleId) && k.getCount() > 0).findFirst().orElse(null);
        if(module == null) return new JsonResponse(JsonResponseType.ERROR, "You havent module in your inventory !"); // TODO System message

        // TODO: multiple same can be attach ! Remove this line
        //if(building.getModules().stream().filter(k->k.getItemId().equals(moduleId)).findFirst().orElse(null) != null) return new JsonResponse(JsonResponseType.ERROR, "Module already attached !"); // TODO System message

        if(building.getModules().size() >= ((ModulableBuilding)building.getTemplate()).getMaxModules()) return new JsonResponse(JsonResponseType.ERROR, "Maximum modules reached !"); // TODO System Message

        if(inventoryService.consumeItem(module, 1))
            building.addModule(module.getTemplateId());
        else
            return new JsonResponse(JsonResponseType.ERROR, "Incorrect items count.");
        buildingService.update(building);

        return new JsonResponse(building);
    }
}
