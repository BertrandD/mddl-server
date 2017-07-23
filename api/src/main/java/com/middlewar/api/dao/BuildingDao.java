package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingDao extends DefaultRepository<BuildingInstance, Long> {

    BuildingInstance findOneByIdAndBaseId(String id, long base);

    List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId);
}
