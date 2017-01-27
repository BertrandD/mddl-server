package com.gameserver.dao;

import com.gameserver.model.Base;
import com.gameserver.model.instances.BuildingInstance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingDao extends MongoRepository<BuildingInstance, String> {

    @Query("{ id : ?0, base.$id : ?1 }")
    BuildingInstance findOneByIdAndBaseId(String id, Base base);

    List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId);
}
