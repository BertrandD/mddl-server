package com.gameserver.services;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.repository.BuildingRepository;
import com.gameserver.tasks.BuildingUpdater;
import com.gameserver.tasks.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingService {

    @Autowired
    BuildingRepository repository;

    public BuildingInstance findOne(String id){
        return repository.findOne(id);
    }

    public BuildingInstance findByBaseAndId(Base base, String id) { return repository.findByBaseAndId(base, id); }

    public List<BuildingInstance> findAll() {
        return repository.findAll();
    }

    public BuildingInstance create(Base base, String buildingId){
        Building b = BuildingData.getInstance().getBuilding(buildingId);
        if(b == null) return null;
        BuildingInstance p = new BuildingInstance(base, b);
        return repository.save(p);
    }

    public void update(BuildingInstance p){ repository.save(p); }

    public void ScheduleUpgrade(BuildingInstance building){
        // TODO: how is managed build time (for each level) ?
        // TODO: insert to database and recover scheduled tasks when restart
        long endupgrade = System.currentTimeMillis() + 30000;
        if(building.getEndsAt() > 0){
            endupgrade = building.getEndsAt() + 30000; // TODO: build Time: default 30 sec
        }

        ThreadPoolManager.getInstance().schedule(new BuildingUpdater(this, building), new Date(endupgrade));
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void delete(String id){
        repository.delete(id);
    }

}
