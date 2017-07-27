package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BuildingDao;
import com.middlewar.api.services.BuildingService;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.instances.BuildingInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingServiceImpl extends DefaultServiceImpl<BuildingInstance, BuildingDao> implements BuildingService {

    @Autowired
    private BuildingDao buildingDao;

    @Override
    public BuildingInstance create(Base base, String buildingId) {
        final Building template = BuildingData.getInstance().getBuilding(buildingId);
        if (template == null) return null;

        return buildingDao.save(new BuildingInstance(base, buildingId));
    }

    public BuildingInstance findByBaseAndId(Base base, long id) {
        return buildingDao.findOneByIdAndBaseId(id, base.getId());
    }

    public List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId) {
        return buildingDao.findByBaseAndBuildingId(base, buildingId);
    }
}
