package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.instances.BuildingInstance;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingService extends DefaultService<BuildingInstance> {
    BuildingInstance create(Base base, String buildingId);
    BuildingInstance findBy(Base base, String id);
    List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId);
}
