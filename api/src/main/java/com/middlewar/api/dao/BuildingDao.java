package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingDao extends DefaultRepository<BuildingInstance, String> {

    @Query("{ id : ?0, base.$id : ?1 }")
    BuildingInstance findOneByIdAndBaseId(String id, Base base);

    List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId);
}
