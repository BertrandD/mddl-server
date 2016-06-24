package com.gameserver.manager;

import com.gameserver.model.buildings.Extractor;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.services.BuildingService;
import com.gameserver.services.BuildingTaskService;
import com.gameserver.services.InventoryService;
import com.gameserver.tasks.mongo.BuildingTask;
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

    private static final String BUILDING_MINE_ID = "mine";
    private static final String BUILDING_PUMP_ID = "pump";
    private static final String BUILDING_STORAGE_ID = "storage";

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
        public void run()
        {
            final BuildingInstance building = getCurrentTask().getBuilding();

            if(building.getBuildingId().equals(BUILDING_STORAGE_ID))
            {
                // Updating resources (lastrefresh) before expanding inventory
                inventoryService.refreshResource(building.getBase());
                // force lastrefresh to be updated (because if no resource was updated (max storage capacity reached), lastrefresh isnt updated)
                inventoryService.forceUpdateLastRefresh(building.getBase().getBaseInventory());
            }

            building.setCurrentLevel(getCurrentTask().getLevel());
            buildingService.update(getCurrentTask().getBuilding());

            if(building.getBuildingId().equals(BUILDING_MINE_ID) && building.getCurrentLevel() == 1)
            {
                final Inventory inventory = building.getBase().getBaseInventory();
                final Extractor mine = (Extractor) building.getTemplate();
                mine.getProduceItems().forEach(k -> inventoryService.addItem(inventory, k.getItemId(), 0));
            }

            if(building.getBuildingId().equals(BUILDING_PUMP_ID) && building.getCurrentLevel() == 1)
            {
                final Inventory inventory = building.getBase().getBaseInventory();
                final Extractor mine = (Extractor) building.getTemplate();
                mine.getProduceItems().forEach(k -> inventoryService.addItem(inventory, k.getItemId(), 0));
            }

            restart(getCurrentTask());
        }
    }
}
