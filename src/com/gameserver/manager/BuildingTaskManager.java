package com.gameserver.manager;

import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.services.InventoryService;
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

    @Autowired
    private InventoryService inventoryService;

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
            task.getBuilding().setStartedAt(-1);
            buildingService.update(task.getBuilding());
        }else{
            final BuildingTask bTask = buildingTaskService.findFirstByBuildingOrderByEndsAtAsc(task.getBuilding().getId());
            bTask.getBuilding().setStartedAt(System.currentTimeMillis());
            buildingService.update(bTask.getBuilding());
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
            final BuildingInstance building = getCurrentTask().getBuilding();

            if(building.getCurrentLevel() == 0 && building.getBuildingId().equals("mine_metal"))
            {
                final ResourceInventory inventory = building.getBase().getResources();
                inventory.setLastRefresh(System.currentTimeMillis());
                inventoryService.update(inventory);
            }

            building.setCurrentLevel(getCurrentTask().getLevel());
            buildingService.update(getCurrentTask().getBuilding());
            restart(getCurrentTask());
        }
    }
}
