package com.gameserver.services;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void deleteAll(){
        repository.deleteAll();
    }

    public void delete(String id){
        repository.delete(id);
    }

}
