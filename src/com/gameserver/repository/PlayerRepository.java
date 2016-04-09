package com.gameserver.repository;

import com.gameserver.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByName(String name);
}
