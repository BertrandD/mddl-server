package com.middlewar.api.dao;

import com.middlewar.core.model.vehicles.Ship;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface ShipDao extends MongoRepository<Ship, String> {
}
