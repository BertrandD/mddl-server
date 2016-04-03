package com.gameserver.repository;

import com.gameserver.model.instances.RecipeInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface RecipeRepository extends MongoRepository<RecipeInstance, String> {
}
