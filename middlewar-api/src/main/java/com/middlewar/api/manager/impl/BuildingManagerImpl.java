package com.middlewar.api.manager.impl;

import com.middlewar.core.exceptions.BuildingAlreadyExistsException;
import com.middlewar.core.exceptions.BuildingCreationException;
import com.middlewar.core.exceptions.BuildingMaxLevelReachedException;
import com.middlewar.core.exceptions.BuildingNotFoundException;
import com.middlewar.core.exceptions.BuildingTemplateNotFoundException;
import com.middlewar.core.exceptions.ItemNotFoundException;
import com.middlewar.core.exceptions.MaximumModulesReachedException;
import com.middlewar.core.exceptions.ModuleNotInInventoryException;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.BuildingTaskManager;
import com.middlewar.api.services.impl.BuildingServiceImpl;
import com.middlewar.api.services.ValidatorService;
import com.middlewar.api.services.impl.InventoryServiceImpl;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.buildings.ModulableBuilding;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.tasks.BuildingTask;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

import static com.middlewar.core.predicate.BuildingInstancePredicate.hasId;

/**
 * @author Bertrand
 */
@Service
@Validated
public class BuildingManagerImpl implements BuildingManager {

    @Autowired
    private BuildingServiceImpl buildingService;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @Autowired
    private ValidatorService validator;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    public BuildingInstance create(@NotNull Base base, @NotEmpty String templateId) {
        base.initializeStats();

        final Building template = BuildingData.getInstance().getBuilding(templateId);
        if (template == null) throw new BuildingTemplateNotFoundException();

        final List<BuildingInstance> existingBuildings = buildingService.findByBaseAndBuildingId(base, templateId);
        if (existingBuildings != null && existingBuildings.size() >= template.getMaxBuild())
            throw new BuildingAlreadyExistsException(); // TODO: SysMsg MAX REACHED !

        final BuildingInstance tempBuilding = new BuildingInstance(base, templateId);

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        validator.validateBuildingRequirements(base, tempBuilding, collector);

        final BuildingInstance building = buildingService.create(base, templateId);
        if (building == null) throw new BuildingCreationException();

        collector.forEach(inventoryService::consumeItem);

        //baseService.update(base); // TODO : do not update a base given in parameter (security)

        buildingTaskManager.ScheduleUpgrade(building);

        return building;
    }

    @Override
    public BuildingInstance upgrade(Base base, long id) {
        // TODO: better selecting by templateId instead of id ?
        final BuildingInstance building = base.getBuildings().stream().filter(hasId(id)).findFirst().orElseThrow(BuildingNotFoundException::new);

        final BuildingTask lastInQueue = null; //buildingTaskService.findFirstByBuildingOrderByEndsAtDesc(building.getId());
        final Building template = building.getTemplate();
        if (building.getCurrentLevel() >= template.getMaxLevel() ||
                (lastInQueue != null && lastInQueue.getLevel() + 1 >= template.getMaxLevel())) {
            throw new BuildingMaxLevelReachedException();
        }

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        validator.validateBuildingRequirements(base, building, collector);

        collector.forEach(inventoryService::consumeItem);

        base.initializeStats();

        buildingTaskManager.ScheduleUpgrade(building);
        return building;
    }

    public BuildingInstance attachModule(Base base, long buildingInstId, String moduleId) {
        base.initializeStats();

        final BuildingInstance building = base.getBuildings().stream()
                .filter(hasId(buildingInstId))
                .findFirst().orElseThrow(BuildingNotFoundException::new);

        if (ItemData.getInstance().getModule(moduleId) == null) throw new ItemNotFoundException();

        final ItemInstance module = base.getBaseInventory().getItems().stream()
                .filter(k -> k.getTemplateId().equals(moduleId))
                .findFirst().orElseThrow(ModuleNotInInventoryException::new);

        //if(building.getModules().stream().filter(k->k.getItemId().equals(moduleId)).findFirst().orElse(null) != null) return new Response(JsonResponseType.ERROR, "Module already attached !"); // TODO System message

        if (building.getModules().size() >= ((ModulableBuilding) building.getTemplate()).getMaxModules())
            throw new MaximumModulesReachedException();
/*
        // TODO: MAKE A TEST !!!!
        if (!((ModulableBuilding) building.getTemplate()).getModules().contains((Module) module.getTemplate()))
            throw new ModuleNotAllowedHereException();

        inventoryService.consumeItem(module, 1);

        building.addModule(module.getTemplateId());
        buildingService.update(building);
*/
        return building;
    }
}
