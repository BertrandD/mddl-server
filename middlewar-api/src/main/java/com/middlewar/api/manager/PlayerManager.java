package com.middlewar.api.manager;

import com.middlewar.core.exceptions.ForbiddenNameException;
import com.middlewar.core.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.core.exceptions.NoPlayerConnectedException;
import com.middlewar.core.exceptions.PlayerCreationFailedException;
import com.middlewar.core.exceptions.PlayerNotFoundException;
import com.middlewar.core.exceptions.PlayerNotOwnedException;
import com.middlewar.core.exceptions.UsernameAlreadyExistsException;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerManager {

    /**
     * @param account Connected account
     * @return the selected player of the current account
     * @throws NoPlayerConnectedException if the account is guest
     * @throws PlayerNotFoundException    if the player of the account is not found
     */
    Player getCurrentPlayerForAccount(Account account);

    /**
     * Get the player with the given id and check if it is matching the given account
     *
     * @param account the account to check if the player
     * @param id      the player id we want
     * @return the player
     * @throws PlayerNotOwnedException if the player is not one of the account's players
     */
    Player getPlayerOfAccount(Account account, long id);

    /**
     * Create a Player for the given account with the given name
     *
     * @param account the account owner of the newly created Player
     * @param name    the name of the newly created Player
     * @return the newly created Player
     * @throws MaxPlayerCreationReachedException if the account has already reached the maximum number of players
     * @throws UsernameAlreadyExistsException    if the player name is already used
     * @throws ForbiddenNameException            if the name is in the name blacklist
     * @throws PlayerCreationFailedException     if the player creation failed
     */
    Player create(Account account, String name);

    /**
     * @param account the account we want the players
     * @return the list of Player of the given account
     */
    List<Player> findAll(Account account);
}
