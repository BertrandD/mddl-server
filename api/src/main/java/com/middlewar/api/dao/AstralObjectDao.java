package com.middlewar.api.dao;

import com.middlewar.core.model.space.AstralObject;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface AstralObjectDao extends MongoRepository<AstralObject, String> {
    AstralObject findOneByName(String name);
}
