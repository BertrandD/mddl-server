package com.gameserver.repository;

import com.gameserver.model.vehicles.Ship;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface ShipRepository extends MongoRepository<Ship, String> {
}
