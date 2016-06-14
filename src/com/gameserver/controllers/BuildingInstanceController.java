package com.gameserver.controllers;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.ItemType;
import com.gameserver.enums.Lang;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.FuncHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.ModulableBuilding;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingService;
import com.gameserver.services.BuildingTaskService;
import com.gameserver.services.InventoryService;
import com.gameserver.services.PlayerService;
import com.gameserver.tasks.mongo.BuildingTask;
import com.util.Evaluator;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.Response.JsonResponseType;
import com.util.data.json.View;
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
import java.util.logging.Logger;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/building", produces = "application/json")
public class BuildingInstanceController {

    private static final Logger logger = Logger.getLogger(BuildingInstanceController.class.getName());

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingTaskService buildingTaskService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private InventoryService inventoryService;

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse findAll(@AuthenticationPrincipal Account pAccount) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);
        return new JsonResponse(base.getBuildings());
    }

    @JsonView(View.buildingInstance_full.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findOne(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        final BuildingInstance building = buildingService.findByBaseAndId(player.getCurrentBase(), id);
        if(building == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BUILDING_NOT_FOUND);
        building.setLang(pAccount.getLang());
        return new JsonResponse(building);
    }

    @JsonView(View.buildingInstance_base.class)
    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "building") String templateId) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        final BuildingInstance hasBuilding = buildingService.findByBaseAndBuildingId(base, templateId);
        if(hasBuilding != null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BUILDING_ALREADY_EXIST);

        /*
        * TODO: uncomment
        if(base.getBuildingPositions().containsKey(position)){
            return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_POSITION_ALREADY_TAKEN);
        }*/

        final BuildingInstance tempBuilding = new BuildingInstance(base, BuildingData.getInstance().getBuilding(templateId));

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        final JsonResponse validate = validateRequirements(tempBuilding, tempBuilding.getTemplate(), collector, pAccount.getLang());
        if(validate != null) return validate;

        final BuildingInstance building = buildingService.create(base, templateId);
        if(building == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BUILDING_CANNOT_CREATE);

        collector.forEach(inventoryService::consumeItem);

        base.addBuilding(building, base.getBuildingPositions().size()+1); // temp position disable
        baseService.update(base);

        buildingService.ScheduleUpgrade(building);

        JsonResponse response = new JsonResponse(building);
        response.addMeta("base", base);
        return response;
    }

    @JsonView(View.buildingInstance_base.class)
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
        final JsonResponse validate = validateRequirements(building, template, collector, pAccount.getLang());
        if(validate != null) return validate;

        collector.forEach(inventoryService::consumeItem);

        buildingService.ScheduleUpgrade(building);
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

    private JsonResponse validateRequirements(BuildingInstance building, Building template, HashMap<ItemInstance, Long> collector, Lang lang) {
        final Requirement requirements = template.getRequirements().get(building.getCurrentLevel()+1);
        if(requirements == null) return null;

        if(!validateBuildings(building.getBase(), requirements)){
            return new JsonResponse(JsonResponseType.ERROR, lang, SystemMessageId.YOU_DONT_MEET_BUILDING_REQUIREMENT);
        }

        if(!validateItems(building.getBase(), requirements, collector)){
            return new JsonResponse(JsonResponseType.ERROR, lang, SystemMessageId.YOU_DONT_MEET_ITEM_REQUIREMENT);
        }

        if(!validateFunctions(building, requirements, collector)){
            return new JsonResponse(JsonResponseType.ERROR, lang, SystemMessageId.YOU_DONT_MEET_RESOURCE_REQUIREMENT);
        }

        return null;
    }

    private boolean validateBuildings(Base base, Requirement requirements) {
        int i = 0;
        boolean meetRequirements = true;
        while(meetRequirements && i < requirements.getBuildings().size())
        {
            final BuildingHolder holder = requirements.getBuildings().get(i);
            final BuildingInstance bInst = base.getBuildings().stream().filter(k -> k.getBuildingId().equals(holder.getId())).findFirst().orElse(null);
            if(bInst == null || bInst.getCurrentLevel() < holder.getLevel()) {
                meetRequirements = false;
            }
            i++;
        }

        return meetRequirements;
    }

    private boolean validateItems(Base base, Requirement requirements, HashMap<ItemInstance, Long> collector) {
        int i = 0;
        boolean meetRequirements = true;
        while(meetRequirements && i < requirements.getItems().size())
        {
            final ItemHolder holder = requirements.getItems().get(i);
            final ItemType itemType = ItemData.getInstance().getTemplate(holder.getId()).getType();

            final Inventory inventory;
            if(itemType.equals(ItemType.RESOURCE)) {
                // todo: refresh resource ?
                inventory = base.getResourcesInventory();
            } else {
                inventory = base.getBaseInventory();
            }

            final ItemInstance iInst = inventory.getItems().stream().filter(k -> k.getTemplateId().equals(holder.getId())).findFirst().orElse(null);
            if(iInst == null || iInst.getCount() < holder.getCount()) {
                meetRequirements = false;
            }

            collector.put(iInst, holder.getCount());
            i++;
        }
        return meetRequirements;
    }

    private boolean validateFunctions(BuildingInstance building, Requirement requirements, HashMap<ItemInstance, Long> collector) {
        int i = 0;
        boolean meetRequirements = true;

        inventoryService.refreshResource(building.getBase());

        while(meetRequirements && i < requirements.getFunctions().size())
        {
            final FuncHolder holder = requirements.getFunctions().get(i);
            final ItemInstance iInst = building.getBase().getResourcesInventory().getItems().stream().filter(k->k.getTemplateId().equals(holder.getItemId())).findFirst().orElse(null);
            final long reqCount = ((Number)Evaluator.getInstance().eval(holder.getFunction().replace("$level", ""+(building.getCurrentLevel()+1)))).longValue();

            if(iInst == null || iInst.getCount() < reqCount) {
                meetRequirements = false;
            }

            collector.put(iInst, reqCount);
            i++;
        }
        return meetRequirements;
    }
}
