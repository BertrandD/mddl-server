package com.gameserver.repository;

import com.gameserver.model.Base;
import com.gameserver.model.instances.BuildingInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface BuildingRepository extends MongoRepository<BuildingInstance, String> {
    BuildingInstance findByBaseAndId(Base base, String id);
    BuildingInstance findByBaseAndBuildingId(Base base, String buildingId);
}
