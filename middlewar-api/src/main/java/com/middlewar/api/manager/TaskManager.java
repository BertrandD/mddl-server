package com.middlewar.api.manager;

import com.middlewar.core.config.Config;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.core.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.PriorityQueue;

/**
 * @author LEBOC Philippe
 */
@Service
@Slf4j
public class TaskManager {

    @Autowired
    private TaskExecutorManager taskExecutorManager;

    public void scheduleUpgrade(BuildingInstance building) {
        final BuildingTask task;
        final long now = TimeUtil.getCurrentTime();
        final BuildingTask lastInQueue = findTaskInQueue(building);

        long buildTime = (long) (building.getBuildTime() * building.getBase().getBaseStat().getValue(Stats.BUILD_COOLDOWN_REDUCTION, Config.BUILDTIME_MODIFIER));
        long endupgrade = now + buildTime;

        if (lastInQueue == null) {
            building.setStartedAt(now); // This value is a false startedAt value ! Difference of ~30 millis
            building.setEndsAt(endupgrade);
            task = new BuildingTask(building.getBase(), building, endupgrade, building.getCurrentLevel() + 1);
        } else {
            endupgrade = lastInQueue.getEndsAt() + (long) (building.getBuildTime() * Config.BUILDTIME_MODIFIER);
            task = new BuildingTask(building.getBase(), building, endupgrade, lastInQueue.getLevel() + 1);
        }

        building.getBase().getBuildingTasks().offer(task);

        log.info("Scheduling upgrade of " + task.getBuilding().getBuildingId() + " to level " + task.getLevel());

        ThreadPoolManager.getInstance().schedule(taskExecutorManager.getExecutor(task), new Date(task.getEndsAt()));
    }

    public static BuildingTask findTaskInQueue(BuildingInstance buildingInstance) {
        PriorityQueue<BuildingTask> queue = buildingInstance.getBase().getBuildingTasks();

        for (BuildingTask b : queue) {
            if (b.getBuilding().equals(buildingInstance))
                return b;
        }
        return null;
    }
}
