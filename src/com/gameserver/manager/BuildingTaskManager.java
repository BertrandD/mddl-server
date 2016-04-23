package com.gameserver.manager;

import com.gameserver.tasks.mongo.BuildingTask;
import com.gameserver.services.BuildingService;
import com.gameserver.services.BuildingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private static List<BuildingTask> tasks = new ArrayList<>();

    private BuildingTask currentTask;

    @PostConstruct
    private void load(){
        setTasks(buildingTaskService.findAll());
        start();
    }

    public void notifyNewTask(BuildingTask task){
        tasks.add(task);

        if(scheduledFuture != null){
            if(task.getEndsAt() < currentTask.getEndsAt()){
                scheduledFuture.cancel(false);
                scheduledFuture = null;
            }
        }

        start();
    }

    public void start(){
        if(tasks.isEmpty()) return;

        if(scheduledFuture == null)
        {
            final BuildingTask task = tasks.stream().sorted((o1, o2) -> (int)o1.compareToAsc(o2)).findFirst().orElse(null);
            if(task == null) return;

            scheduledFuture = ThreadPoolManager.getInstance().schedule(new Upgrade(), new Date(task.getEndsAt()));

            if(scheduledFuture != null){
                currentTask = task;
            }
        }
    }

    public void restart(BuildingTask task){
        tasks.remove(task);
        buildingTaskService.delete(task);

        if(buildingTaskService.findByBuilding(task.getBuilding().getId()).isEmpty()){
            task.getBuilding().setIsInQueue(false);
            buildingService.update(task.getBuilding());
        }

        scheduledFuture = null;
        currentTask = null;
        start();
    }

    public void setTasks(List<BuildingTask> tasks) {
        BuildingTaskManager.tasks = tasks;
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
