package com.middlewar.api.manager;

import com.middlewar.api.exceptions.*;
import com.middlewar.api.services.*;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.buildings.ModulableBuilding;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.tasks.BuildingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author Bertrand
 */
@Service
public class BuildingManager {
    @Autowired
    private BaseManager baseManager;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validator;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    @Autowired
    private BuildingTaskService buildingTaskService;

    public BuildingInstance getBuilding(Base base, String baseId) throws BuildingNotFoundException {
        final BuildingInstance building = buildingService.findBy(base, baseId);
        if(building == null) throw new BuildingNotFoundException();

        return building;
    }

    public BuildingInstance create(Base base, String templateId) throws BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingRequirementMissingException, BuildingCreationException {
        base.initializeStats();

        final Building template = BuildingData.getInstance().getBuilding(templateId);
        if(template == null) throw new BuildingTemplateNotFoundException();

        final List<BuildingInstance> existingBuildings = buildingService.findByBaseAndBuildingId(base, templateId);
        if(existingBuildings != null && existingBuildings.size() >= template.getMaxBuild()) throw new BuildingAlreadyExistsException(); // TODO: SysMsg MAX REACHED !

        final BuildingInstance tempBuilding = new BuildingInstance(base, templateId);

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        validator.validateBuildingRequirements(base, tempBuilding, collector);

        final BuildingInstance building = buildingService.create(base, templateId);
        if(building == null) throw new BuildingCreationException();

        collector.forEach(inventoryService::consumeItem);

        baseService.update(base); // TODO : do not update a base given in parameter (security)

        buildingTaskManager.ScheduleUpgrade(building);

        return building;
    }

    public BuildingInstance getBuildingOfBase(Base base, String id) throws BuildingNotFoundException {
        final BuildingInstance building = base.getBuildings().stream().filter(k->k.getId().equals(id)).findFirst().orElse(null);
        if(building == null){
            throw new BuildingNotFoundException();
        }

        return building;
    }

    public BuildingInstance upgrade(Base base, String id) throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        final BuildingInstance building = getBuildingOfBase(base, id);

        final BuildingTask lastInQueue = buildingTaskService.findFirstByBuildingOrderByEndsAtDesc(building.getId());
        final Building template = building.getTemplate();
        if(building.getCurrentLevel() >= template.getMaxLevel() ||
                (lastInQueue != null && lastInQueue.getLevel() + 1 >= template.getMaxLevel())){
            throw new BuildingMaxLevelReachedException();
        }

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        validator.validateBuildingRequirements(base, building, collector);

        collector.forEach(inventoryService::consumeItem);

        base.initializeStats();

        buildingTaskManager.ScheduleUpgrade(building);
        return building;
    }

    public BuildingInstance attachModule(Base base, String buildingInstId, String moduleId) throws BuildingNotFoundException, ModuleNotInInventoryException, MaximumModulesReachedException, ModuleNotAllowedHereException, NotEnoughModulesException {
        base.initializeStats();

        final BuildingInstance building = getBuildingOfBase(base, buildingInstId);

        final ItemInstance module = base.getBaseInventory().getItemsToMap().get(moduleId);
        if(module == null) throw new ModuleNotInInventoryException();

        //if(building.getModules().stream().filter(k->k.getItemId().equals(moduleId)).findFirst().orElse(null) != null) return new Response(JsonResponseType.ERROR, "Module already attached !"); // TODO System message

        if(building.getModules().size() >= ((ModulableBuilding)building.getTemplate()).getMaxModules())
            throw new MaximumModulesReachedException();

        // TODO: MAKE A TEST !!!!
        if(!((ModulableBuilding) building.getTemplate()).getModules().contains((Module)module.getTemplate()))
            throw new ModuleNotAllowedHereException();

        if(!inventoryService.consumeItem(module, 1))
            throw new NotEnoughModulesException();

        building.addModule(module.getTemplateId());
        buildingService.update(building);

        return building;
    }
}
