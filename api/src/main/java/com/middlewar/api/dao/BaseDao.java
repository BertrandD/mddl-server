package com.gameserver.dao;

import com.gameserver.model.Base;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface BaseDao extends MongoRepository<Base, String> {
}
