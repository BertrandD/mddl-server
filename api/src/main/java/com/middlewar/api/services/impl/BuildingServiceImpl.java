package com.gameserver.services.impl;

import com.gameserver.dao.BuildingDao;
import com.gameserver.data.xml.BuildingData;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingDao buildingDao;

    @Override
    public BuildingInstance create(Base base, String buildingId) {
        final Building template = BuildingData.getInstance().getBuilding(buildingId);
        if(template == null) return null;

        return buildingDao.insert(new BuildingInstance(base, buildingId));
    }

    public BuildingInstance findBy(Base base, String id) {
        return buildingDao.findOneByIdAndBaseId(id, base);
    }

    public List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId) {
        return buildingDao.findByBaseAndBuildingId(base, buildingId);
    }

    @Override
    public BuildingInstance findOne(String id) {
        return buildingDao.findOne(id);
    }

    @Override
    public List<BuildingInstance> findAll() {
        return buildingDao.findAll();
    }

    @Override
    public void update(BuildingInstance object) {
        buildingDao.save(object);
    }

    @Override
    public void remove(BuildingInstance object) {
        buildingDao.delete(object);
    }

    @Override
    public void clearAll() {
        buildingDao.deleteAll();
    }
}
