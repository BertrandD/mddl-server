package com.middlewar.api.services;

import com.middlewar.api.dao.BuildingDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingService extends DefaultService<BuildingInstance, BuildingDao> {
    BuildingInstance create(Base base, String buildingId);

    BuildingInstance findByBaseAndId(Base base, long id);

    List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId);
}
