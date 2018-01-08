package com.middlewar.api.services;

import com.middlewar.core.exception.PlayerNotOwnedException;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerService extends CrudService<Player, Integer> {

    List<Player> findAll(@NotNull final Account account);

    /**
     * Get the player with the given id and check if it is matching the given account
     *
     * @param account the account to check if the player
     * @param id      the player id we want
     * @return the player
     * @throws PlayerNotOwnedException if the player is not one of the account's players
     */
    Player find(@NotNull final Account account, final int id);
}
