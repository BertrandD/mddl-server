package com.middlewar.api.services;

import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerService extends DefaultService<Player> {
    /**
     * Create a new Player
     *
     * @param account account that's request the new creation (Warning: this variable come from the @AuthenticationPrincipal)
     * @param name    the name for the new player
     * @return the new created player
     */
    Player create(Account account, String name);

    /**
     * @param account the given account
     * @return all player from the given account
     */
    List<Player> findByAccount(Account account);

    /**
     * @param name the name of the player to retrieve from the database
     * @return a player that find the given name
     */
    Player findByName(String name);
}
