package com.middlewar.api.manager.impl;

import com.middlewar.api.services.BaseService;
import com.middlewar.core.exceptions.BaseNotOwnedException;
import com.middlewar.core.exceptions.BuildingAlreadyExistsException;
import com.middlewar.core.exceptions.BuildingMaxLevelReachedException;
import com.middlewar.core.exceptions.BuildingNotFoundException;
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
import com.middlewar.core.model.Player;
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

import static com.middlewar.core.predicate.BasePredicate.hasId;
import static com.middlewar.core.predicate.BuildingInstancePredicate.hasTemplateId;

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
    private BaseService baseService;

    @Autowired
    private ValidatorService validator;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    public BuildingInstance create(@NotNull Player player, int baseId, @NotEmpty String templateId) {

        final Base base = player.getBases().stream().filter(hasId(baseId)).findFirst().orElseThrow(BaseNotOwnedException::new);
        base.initializeStats();

        final Building template = BuildingData.getInstance().getBuilding(templateId);

        final BuildingInstance existBuilding = base.getBuildings().stream().filter(hasTemplateId(templateId)).findFirst().orElse(null);
        if(existBuilding != null) throw new BuildingAlreadyExistsException();

        // TODO: validate template requirements !
        // List<ItemInstanceHolder> collector = requirementUtil.isValid();
        // collector.forEach(inventoryService::consumeItem);

        final BuildingInstance building = buildingService.create(base, templateId);
        base.addBuilding(building);

        baseService.updateAsync(base);
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
