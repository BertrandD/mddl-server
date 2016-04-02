package com.gameserver.repository;

import com.gameserver.model.Base;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface BaseRepository extends MongoRepository<Base, String> {
}
