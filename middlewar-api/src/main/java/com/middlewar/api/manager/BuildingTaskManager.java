package com.middlewar.api.manager;

import com.middlewar.api.services.BuildingService;
import com.middlewar.api.services.BuildingTaskService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.config.Config;
import com.middlewar.core.enums.BuildingCategory;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.ModulableBuilding;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.core.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingTaskManager {

    private static final String BUILDING_MINE_ID = "mine";
    private static final String BUILDING_PUMP_ID = "pump";
    @Autowired
    private BuildingTaskService buildingTaskService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private InventoryService inventoryService;
    private ScheduledFuture<?> scheduledFuture;
    private BuildingTask currentTask;

    @PostConstruct
    private void load() {
        start();
    }

    public void notifyNewTask(BuildingTask task) {
        if (scheduledFuture != null) {
            if (task.getEndsAt() < currentTask.getEndsAt()) {
                scheduledFuture.cancel(false);
                scheduledFuture = null;
            }
        }
        start();
    }

    public void start() {
        if (scheduledFuture == null) {
            final BuildingTask task = buildingTaskService.findFirstByOrderByEndsAtAsc();
            if (task == null) return;

            task.getBuilding().setEndsAt(task.getEndsAt());
            buildingService.update(task.getBuilding());

            scheduledFuture = ThreadPoolManager.getInstance().schedule(new Upgrade(), new Date(task.getEndsAt()));

            if (scheduledFuture != null) {
                currentTask = task;
            }
        }
    }

    public void restart() {
        restart(false);
    }

    public void restart(boolean mayInterruptIfRunning) {
        if (scheduledFuture != null)
            scheduledFuture.cancel(mayInterruptIfRunning);
        scheduledFuture = null;
        currentTask = null;
        start();
    }

    public void ScheduleUpgrade(BuildingInstance building) {
        final BuildingTask newTask;
        final long now = TimeUtil.getCurrentTime();
        final BuildingTask lastInQueue = buildingTaskService.findFirstByBuildingOrderByEndsAtDesc(building.getId());

        long buildTime = (long) (building.getBuildTime() * building.getBase().getBaseStat().getValue(Stats.BUILD_COOLDOWN_REDUCTION, Config.BUILDTIME_MODIFIER));
        long endupgrade = now + buildTime;

        if (lastInQueue == null) {
            building.setStartedAt(now); // This value is a false startedAt value ! Difference of ~30 millis
            building.setEndsAt(endupgrade);
            buildingService.update(building);
            newTask = buildingTaskService.create(building, endupgrade, building.getCurrentLevel() + 1);
        } else {
            endupgrade = lastInQueue.getEndsAt() + (long) (building.getBuildTime() * Config.BUILDTIME_MODIFIER);
            newTask = buildingTaskService.create(building, endupgrade, lastInQueue.getLevel() + 1);
        }

        notifyNewTask(newTask);
    }

    public BuildingTask getCurrentTask() {
        return currentTask;
    }

    private class Upgrade implements Runnable {
        @Override
        public synchronized void run() {
            final BuildingInstance building = getCurrentTask().getBuilding();

//            if (building.getTemplate().getType().equals(BuildingCategory.SILO))
//                inventoryService.refreshResources(building.getBase());

            building.setCurrentLevel(getCurrentTask().getLevel());
            buildingTaskService.delete(getCurrentTask());

            if (building.getCurrentLevel() == 1) {
                building.getBase().addBuilding(building);
            }

            if (buildingTaskService.findByBuilding(building.getId()).isEmpty()) {
                building.setEndsAt(-1);
                building.setStartedAt(-1);
            } else {
                final BuildingTask bTask = buildingTaskService.findFirstByBuildingOrderByEndsAtAsc(building.getId());
                bTask.getBuilding().setStartedAt(TimeUtil.getCurrentTime());
                buildingService.update(bTask.getBuilding());
            }

            buildingService.update(building);

            // TODO : refactor this because it is not generic. This logic should not be here. Maybe in Base::addBuilding() ?
            if (building.getBuildingId().equals(BUILDING_MINE_ID) && building.getCurrentLevel() == 1) {
                final Base base = building.getBase();
                final ModulableBuilding mine = (ModulableBuilding) building.getTemplate();
                mine.getAllStats().forEach(k -> inventoryService.createNewResource(base, k.getStat().name().toLowerCase()));
            }

            if (building.getBuildingId().equals(BUILDING_PUMP_ID) && building.getCurrentLevel() == 1) {
                final Base base = building.getBase();
                final ModulableBuilding pump = (ModulableBuilding) building.getTemplate();
                pump.getAllStats().forEach(k -> inventoryService.createNewResource(base, k.getStat().name().toLowerCase()));
            }

            restart();
        }
    }
}
