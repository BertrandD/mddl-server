package com.gameserver.manager;

import com.gameserver.model.buildings.Extractor;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.services.BuildingService;
import com.gameserver.services.BuildingTaskService;
import com.gameserver.services.InventoryService;
import com.gameserver.tasks.mongo.BuildingTask;
import com.util.Utils;
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

    public void notifyNewTask(BuildingTask task) {
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

    public void restart(){
        scheduledFuture = null;
        currentTask = null;
        Utils.println("Scheduled task finished");
        start();
    }

    public BuildingTask getCurrentTask() {
        return currentTask;
    }

    private class Upgrade implements Runnable
    {
        @Override
        public synchronized void run()
        {
            Utils.println("The task is null ? "+getCurrentTask());
            Utils.println("The building from task is null ? "+getCurrentTask().getBuilding());
            final BuildingInstance building = getCurrentTask().getBuilding();

            if(building.getBuildingId().equals(BUILDING_STORAGE_ID))
            {
                // Updating resources (lastrefresh) before expanding inventory
                inventoryService.refreshResource(building.getBase());
            }

            building.setCurrentLevel(getCurrentTask().getLevel());
            buildingTaskService.delete(getCurrentTask());

            if(buildingTaskService.findByBuilding(building.getId()).isEmpty()){
                building.setEndsAt(-1);
                building.setStartedAt(-1);
            }else{
                final BuildingTask bTask = buildingTaskService.findFirstByBuildingOrderByEndsAtAsc(building.getId());
                bTask.getBuilding().setStartedAt(System.currentTimeMillis());
                buildingService.update(bTask.getBuilding());
            }

            buildingService.update(building);

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

            restart();
        }
    }
}
