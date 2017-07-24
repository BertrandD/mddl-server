package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingService extends DefaultService<BuildingInstance> {
    BuildingInstance create(Base base, String buildingId);
    BuildingInstance findBy(Base base, long id);
    List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId);
}
