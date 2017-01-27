package com.gameserver.dao;

import com.gameserver.model.vehicles.Ship;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface ShipDao extends MongoRepository<Ship, String> {
}
