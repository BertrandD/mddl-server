package com.middlewar.api.manager.impl;

import com.middlewar.api.services.BaseService;
import com.middlewar.core.exception.BaseNotOwnedException;
import com.middlewar.core.exception.BuildingAlreadyExistsException;
import com.middlewar.core.exception.BuildingMaxLevelReachedException;
import com.middlewar.core.exception.BuildingNotFoundException;
import com.middlewar.core.exception.BuildingNotOwnedException;
import com.middlewar.core.exception.ItemNotFoundException;
import com.middlewar.core.exception.MaximumModulesReachedException;
import com.middlewar.core.exception.ModuleNotInInventoryException;
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
import com.middlewar.core.predicate.BasePredicate;
import com.middlewar.core.predicate.BuildingInstancePredicate;
import com.middlewar.core.predicate.BuildingTaskPredicate;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

import static com.middlewar.core.predicate.BuildingInstancePredicate.hasId;
import static com.middlewar.core.predicate.BuildingInstancePredicate.hasTemplateId;
import static java.util.Comparator.comparing;

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

        final Base base = player.getBases().stream().filter(BasePredicate.hasId(baseId)).findFirst().orElseThrow(BaseNotOwnedException::new);
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
    public BuildingInstance upgrade(@NotNull Player player, int baseId, int buildingId) {

        final Base base = player.getBases().stream().filter(BasePredicate.hasId(baseId)).findFirst().orElseThrow(BaseNotOwnedException::new);
        final BuildingInstance building = base.getBuildings().stream().filter(hasId(buildingId)).findFirst().orElseThrow(BuildingNotOwnedException::new);
        final Building template = building.getTemplate();

        final BuildingTask lastInQueue = base.getBuildingTasks().stream()
                .filter(BuildingTaskPredicate.hasId(building.getId()))
                .min(comparing(BuildingTask::getEndsAt))
                .orElse(null);

        if (building.getCurrentLevel() >= template.getMaxLevel() ||
            lastInQueue != null && lastInQueue.getLevel() + 1 >= template.getMaxLevel()) {
            throw new BuildingMaxLevelReachedException();
        }

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        validator.validateBuildingRequirements(base, building, collector);

        collector.forEach(inventoryService::consumeItem);

        base.initializeStats();

        buildingTaskManager.ScheduleUpgrade(building);
        return building;
    }

    public BuildingInstance attachModule(@NotNull Player player, int baseId, int buildingId, @NotEmpty String moduleId) {

        final Base base = player.getBases().stream().filter(BasePredicate.hasId(baseId)).findFirst().orElseThrow(BaseNotOwnedException::new);
        base.initializeStats();

        final BuildingInstance building = base.getBuildings().stream()
                .filter(BuildingInstancePredicate.hasId(buildingId))
                .findFirst().orElseThrow(BuildingNotFoundException::new);

        if (ItemData.getInstance().getModule(moduleId) == null) throw new ItemNotFoundException();

        // TODO: cleanup required from here
        final ItemInstance module = base.getBaseInventory().getItems().stream()
                .filter(k -> k.getTemplateId().equals(moduleId))
                .findFirst().orElseThrow(ModuleNotInInventoryException::new);

        //if(building.getModules().stream().filter(k->k.getItemId().equals(moduleId)).findFirst().orElse(null) != null) return new Response(JsonResponseType.ERROR, "Module already attached !"); // TODO System message

        if (building.getModules().size() >= ((ModulableBuilding) building.getTemplate()).getMaxModules())
            throw new MaximumModulesReachedException();
        /*
        if (!((ModulableBuilding) building.getTemplate()).getModules().contains((Module) module.getTemplate())) throw new ModuleNotAllowedHereException();
        inventoryService.consumeItem(module, 1);
        building.addModule(module.getTemplateId());
        buildingService.update(building);
        */
        return building;
    }
}
