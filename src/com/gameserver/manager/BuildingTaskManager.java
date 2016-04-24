package com.gameserver.manager;

import com.gameserver.tasks.mongo.BuildingTask;
import com.gameserver.services.BuildingService;
import com.gameserver.services.BuildingTaskService;
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

    @Autowired
    private BuildingTaskService buildingTaskService;

    @Autowired
    private BuildingService buildingService;

    private ScheduledFuture<?> scheduledFuture;

    private BuildingTask currentTask;

    @PostConstruct
    private void load(){
        start();
    }

    public void notifyNewTask(BuildingTask task){

        if(scheduledFuture != null){
            if(task.getEndsAt() < currentTask.getEndsAt()){
                scheduledFuture.cancel(false);
                scheduledFuture = null;
            }
        }

        start();
    }

    public void start(){
        if(scheduledFuture == null)
        {
            final BuildingTask task = buildingTaskService.findFirstByOrderByEndsAtAsc();
            if(task == null) return;

            task.getBuilding().setEndsAt(task.getEndsAt());
            task.getBuilding().setStartedAt(System.currentTimeMillis());
            buildingService.update(task.getBuilding());

            scheduledFuture = ThreadPoolManager.getInstance().schedule(new Upgrade(), new Date(task.getEndsAt()));

            if(scheduledFuture != null){
                currentTask = task;
            }
        }
    }

    public void restart(BuildingTask task){
        buildingTaskService.delete(task);

        if(buildingTaskService.findByBuilding(task.getBuilding().getId()).isEmpty()){
            task.getBuilding().setEndsAt(-1);
            buildingService.update(task.getBuilding());
        }

        scheduledFuture = null;
        currentTask = null;
        start();
    }

    public BuildingTask getCurrentTask() {
        return currentTask;
    }

    private class Upgrade implements Runnable
    {
        @Override
        public void run() {
            getCurrentTask().getBuilding().setCurrentLevel(getCurrentTask().getLevel());
            buildingService.update(getCurrentTask().getBuilding());
            restart(getCurrentTask());
        }
    }
}
