package com.middlewar.api.executor;

import com.middlewar.api.services.InventoryService;
import com.middlewar.core.enums.BuildingCategory;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.ModulableBuilding;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.core.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import static com.middlewar.api.manager.TaskManager.findTaskInQueue;

@Slf4j
public class BuildingTaskExecutor extends TaskExecutor {

    private static final String BUILDING_MINE_ID = "mine";
    private static final String BUILDING_PUMP_ID = "pump";

    private BuildingTask buildingTask;
    private InventoryService inventoryService;

    public BuildingTaskExecutor(BuildingTask buildingTask, InventoryService inventoryService) {
        this.buildingTask = buildingTask;
        this.inventoryService = inventoryService;
    }

    @Override
    public synchronized void run() {
        final BuildingInstance building = buildingTask.getBuilding();
        log.info("End of upgrade for " + buildingTask.getBuilding().getBuildingId());
        buildingTask.setEndsAt(-1);

        if (building.getTemplate().getType().equals(BuildingCategory.SILO))
            inventoryService.refreshResources(building.getBase());

        building.setCurrentLevel(buildingTask.getLevel());
        if (building.getCurrentLevel() == 1) {
            building.getBase().addBuilding(building);
        }
        building.getBase().getBuildingTasks().remove(buildingTask);

        final BuildingTask lastInQueue = findTaskInQueue(building);

        if (lastInQueue == null) {
            building.setEndsAt(-1);
            building.setStartedAt(-1);
        } else {
            lastInQueue.getBuilding().setStartedAt(TimeUtil.getCurrentTime());
        }

        if (building.getBuildingId().equals(BUILDING_MINE_ID) && building.getCurrentLevel() == 1) {
            final Base base = building.getBase();
            final ModulableBuilding mine = (ModulableBuilding) building.getTemplate();
            mine.getStats().getStatFunctions().forEach((k, v) -> inventoryService.createNewResource(base, k.name().toLowerCase()));
        }

        if (building.getBuildingId().equals(BUILDING_PUMP_ID) && building.getCurrentLevel() == 1) {
            final Base base = building.getBase();
            final ModulableBuilding pump = (ModulableBuilding) building.getTemplate();
            pump.getStats().getStatFunctions().forEach((k, v) -> inventoryService.createNewResource(base, k.name().toLowerCase()));
        }
    }
}
