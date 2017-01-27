package com.gameserver.services;

import com.auth.Account;
import com.gameserver.model.Player;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerService extends DefaultService<Player> {
    Player create(Account account, String name);
    List<Player> findBy(Account account);
    Player findByName(String name);
}
