package com.gameserver.services;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.tasks.mongo.BuildingTask;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.repository.BuildingRepository;
import com.gameserver.manager.BuildingTaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingService {

    @Autowired
    private BuildingRepository repository;

    @Autowired
    private BuildingTaskService buildingTaskService;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    public BuildingInstance findOne(String id){
        return repository.findOne(id);
    }

    public BuildingInstance findByBaseAndId(Base base, String id) { return repository.findByBaseAndId(base, id); }

    public BuildingInstance findByBaseAndBuildingId(Base base, String buildingId) { return repository.findByBaseAndBuildingId(base, buildingId); }

    public List<BuildingInstance> findAll() {
        return repository.findAll();
    }

    public BuildingInstance create(Base base, String buildingId){
        Building b = BuildingData.getInstance().getBuilding(buildingId);
        if(b == null) return null;
        BuildingInstance p = new BuildingInstance(base, b);
        return repository.save(p);
    }

    public void update(BuildingInstance p) {
        repository.save(p);
    }

    public void ScheduleUpgrade(BuildingInstance building){
        // TODO: how is managed build time (for each level) ?

        BuildingTask newTask;
        final BuildingTask lastInQueue = buildingTaskService.findFirstByBuildingOrderByEndsAtDesc(building.getId());
        long endupgrade = System.currentTimeMillis() + 30000;

        if(lastInQueue == null){
            building.setEndsAt(endupgrade);
            update(building);
            newTask = buildingTaskService.create(building, endupgrade, building.getCurrentLevel()+1);
        }else{
            endupgrade = lastInQueue.getEndsAt() + 30000; // TODO: build Time: default 30 sec
            newTask = buildingTaskService.create(building, endupgrade, lastInQueue.getLevel()+1);
        }

        buildingTaskManager.notifyNewTask(newTask);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void delete(String id){
        repository.delete(id);
    }

}
