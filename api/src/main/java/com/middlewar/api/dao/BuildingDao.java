package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingDao extends DefaultRepository<BuildingInstance, String> {

    BuildingInstance findOneByIdAndBaseId(String id, String base);

    List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId);
}
