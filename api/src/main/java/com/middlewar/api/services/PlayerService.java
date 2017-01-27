package com.middlewar.api.services;

import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerService extends DefaultService<Player> {
    Player create(Account account, String name);
    List<Player> findBy(Account account);
    Player findByName(String name);
}
