package com.middlewar.api.dao;

import com.middlewar.core.model.Player;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerDao extends DefaultRepository<Player, String> {
    List<Player> findByAccountId(String accountId);
    Player findByName(String name);
}
