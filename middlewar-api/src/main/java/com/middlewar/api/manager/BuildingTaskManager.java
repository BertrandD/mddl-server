package com.middlewar.api.manager;

import com.middlewar.api.services.impl.BuildingServiceImpl;
import com.middlewar.api.services.impl.InventoryServiceImpl;
import com.middlewar.core.config.Config;
import com.middlewar.core.enums.BuildingCategory;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.ModulableBuilding;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.core.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.ScheduledFuture;

/**
 * @author LEBOC Philippe
 */
@Service
@Slf4j
public class BuildingTaskManager {

    private static final String BUILDING_MINE_ID = "mine";
    private static final String BUILDING_PUMP_ID = "pump";

    //@Autowired
    //private BuildingTaskService buildingTaskService;

    @Autowired
    private BuildingServiceImpl buildingService;

    @Autowired
    private InventoryServiceImpl inventoryService;

    private ScheduledFuture<?> scheduledFuture;
    private BuildingTask currentTask;

    private PriorityQueue<BuildingTask> queue = new PriorityQueue<>();

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
            final BuildingTask task = queue.peek();
            if (task == null) return;

            task.getBuilding().setEndsAt(task.getEndsAt());

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

    public BuildingTask findTaskInQueue(BuildingInstance buildingInstance) {
        PriorityQueue<BuildingTask> queue = buildingInstance.getBase().getBuildingTasks();

        for (BuildingTask b : queue) {
            if (b.getBuilding().equals(buildingInstance))
                return b;
        }
        return null;
    }

    public void ScheduleUpgrade(BuildingInstance building) {
        final BuildingTask newTask;
        final long now = TimeUtil.getCurrentTime();
        final BuildingTask lastInQueue = findTaskInQueue(building);

        long buildTime = (long) (building.getBuildTime() * building.getBase().getBaseStat().getValue(Stats.BUILD_COOLDOWN_REDUCTION, Config.BUILDTIME_MODIFIER));
        long endupgrade = now + buildTime;

        if (lastInQueue == null) {
            building.setStartedAt(now); // This value is a false startedAt value ! Difference of ~30 millis
            building.setEndsAt(endupgrade);
            newTask = new BuildingTask(building.getBase(), building, endupgrade, building.getCurrentLevel() + 1);
        } else {
            endupgrade = lastInQueue.getEndsAt() + (long) (building.getBuildTime() * Config.BUILDTIME_MODIFIER);
            newTask = new BuildingTask(building.getBase(), building, endupgrade, lastInQueue.getLevel() + 1);
        }

        building.getBase().getBuildingTasks().offer(newTask);
        queue.offer(newTask);

        log.info("Scheduling upgrade of " + newTask.getBuilding().getTemplateId() + " to level " + newTask.getLevel());
        notifyNewTask(newTask);
    }

    public BuildingTask getCurrentTask() {
        return currentTask;
    }

    private class Upgrade implements Runnable {
        @Override
        public synchronized void run() {
            BuildingTask buildingTask = queue.poll();
            final BuildingInstance building = buildingTask.getBuilding();
            log.info("End of upgrade for " + buildingTask.getBuilding().getTemplateId());
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

            if (building.getTemplateId().equals(BUILDING_MINE_ID) && building.getCurrentLevel() == 1) {
                final Base base = building.getBase();
                final ModulableBuilding mine = (ModulableBuilding) building.getTemplate();
                mine.getStats().getStatFunctions().forEach((k, v) -> inventoryService.createNewResource(base, k.name().toLowerCase()));
            }

            if (building.getTemplateId().equals(BUILDING_PUMP_ID) && building.getCurrentLevel() == 1) {
                final Base base = building.getBase();
                final ModulableBuilding pump = (ModulableBuilding) building.getTemplate();
                pump.getStats().getStatFunctions().forEach((k, v) -> inventoryService.createNewResource(base, k.name().toLowerCase()));
            }

            restart();
        }
    }
}
