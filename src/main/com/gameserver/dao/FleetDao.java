package com.gameserver.dao;

import com.gameserver.model.vehicles.Fleet;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface FleetDao extends MongoRepository<Fleet, String> {
}
