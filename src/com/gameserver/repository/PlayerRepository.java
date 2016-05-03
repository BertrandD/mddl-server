package com.gameserver.repository;

import com.auth.Account;
import com.gameserver.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByName(String name);
    List<Player> findByAccount(Account account);
}
