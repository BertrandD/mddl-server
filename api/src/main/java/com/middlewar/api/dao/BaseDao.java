package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface BaseDao extends MongoRepository<Base, String> {
}
