package com.middlewar.api.dao;

import com.middlewar.core.model.vehicles.Fleet;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface FleetDao extends MongoRepository<Fleet, String> {
}
