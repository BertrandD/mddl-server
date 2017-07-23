package com.middlewar.api.dao;

import com.middlewar.core.model.Player;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerDao extends DefaultRepository<Player, Long> {
    List<Player> findByAccountId(long accountId);
    Player findByName(String name);
}
