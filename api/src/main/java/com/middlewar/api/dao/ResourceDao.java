package com.middlewar.api.dao;

import com.middlewar.core.model.inventory.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface ResourceDao extends MongoRepository<Resource, String> {
}
