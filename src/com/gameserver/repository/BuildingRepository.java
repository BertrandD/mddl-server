package com.gameserver.repository;

import com.gameserver.model.instances.BuildingInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface BuildingRepository extends MongoRepository<BuildingInstance, String> {
}
