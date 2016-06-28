package com.gameserver.services;

import com.gameserver.model.tasks.BuildingTask;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.repository.BuildingTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingTaskService {

    @Autowired
    private BuildingTaskRepository repository;

    public List<BuildingTask> findAll(){
        return repository.findAll();
    }

    public BuildingTask findFirstByOrderByEndsAtAsc(){
        return repository.findFirstByOrderByEndsAtAsc();
    }

    public List<BuildingTask> findByBuilding(String id) { return repository.findByBuilding(id); }
    public List<BuildingTask> findByBuildingOrderByEndsAtAsc(String id) { return repository.findByBuildingOrderByEndsAtAsc(id); }

    public List<BuildingTask> findByBaseOrderByEndsAtAsc(String id) { return repository.findByBaseOrderByEndsAtAsc(id); }

    public BuildingTask findFirstByBuildingOrderByEndsAtAsc(String id){
        return repository.findFirstByBuildingOrderByEndsAtAsc(id);
    }

    public BuildingTask findFirstByBuildingOrderByEndsAtDesc(String id){
        return repository.findFirstByBuildingOrderByEndsAtDesc(id);
    }

    public BuildingTask create(BuildingInstance building, long timestamp, int level){
        return repository.save(new BuildingTask(building.getBase(), building, timestamp, level));
    }

    public void delete(BuildingTask o){
        delete(o.getId());
    }

    public void delete(String id){
        repository.delete(id);
    }
}
