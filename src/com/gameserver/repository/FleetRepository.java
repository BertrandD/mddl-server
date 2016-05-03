package com.gameserver.repository;

import com.gameserver.model.vehicles.Fleet;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface FleetRepository extends MongoRepository<Fleet, String> {
}
