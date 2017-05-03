package com.middlewar.api.dao;

import com.middlewar.core.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerDao extends MongoRepository<Player, String> {
    List<Player> findByAccountId(String accountId);
    Player findByName(String name);
}
