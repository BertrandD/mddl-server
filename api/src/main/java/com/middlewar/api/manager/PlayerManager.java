package com.middlewar.api.manager;

import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.PlayerNotOwnedException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Bertrand
 */
@Service
public class PlayerManager {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Autowired
    private PlayerService playerService;

    /**
     * @param account Connected account
     * @return the selected player of the current account
     * @throws NoPlayerConnectedException if the account is guest
     * @throws PlayerNotFoundException if the player of the account is not found
     */
    public Player getCurrentPlayerForAccount(Account account) throws NoPlayerConnectedException, PlayerNotFoundException {
        if (account.getCurrentPlayer() == null) {
            throw new NoPlayerConnectedException();
        } else {
            final Player player = playerService.findOne(account.getCurrentPlayer());
            if (player == null) {
                throw new PlayerNotFoundException();
            }
            return player;
        }
    }

    /**
     * Get the player with the given id and check if it is matching the given account
     * @param account the account to check if the player
     * @param id the player id we want
     * @return the player
     * @throws PlayerNotFoundException if the player of the account is not found
     * @throws PlayerNotOwnedException if the player is not one of the account's players
     */
    public Player getPlayerOfAccount(Account account, String id) throws PlayerNotFoundException, PlayerNotOwnedException {
        if (!account.getPlayers().contains(id)) {
            throw new PlayerNotOwnedException();
        }
        final Player player = playerService.findOne(id);
        if(player == null) throw new PlayerNotFoundException();
        if (!player.getAccount().getId().equals(account.getId())) {
            throw new PlayerNotOwnedException();
        }
        return player;
    }


    /**
     * Create a Player for the given account with the given name
     * @param account the account owner of the newly created Player
     * @param name the name of the newly created Player
     * @return the newly created Player
     * @throws MaxPlayerCreationReachedException if the account has already reached the maximum number of players
     * @throws UsernameAlreadyExistsException if the player name is already used
     * @throws ForbiddenNameException if the name is in the name blacklist
     * @throws PlayerCreationFailedException if the player creation failed
     */
    public Player createForAccount(Account account, String name) throws MaxPlayerCreationReachedException, UsernameAlreadyExistsException, ForbiddenNameException, PlayerCreationFailedException {
        Assert.notNull(name, SystemMessageId.INVALID_PARAMETERS);

        if(account.getPlayers().size() >= Config.MAX_PLAYER_IN_ACCOUNT)
            throw new MaxPlayerCreationReachedException();

        if(playerService.findByName(name) != null) throw new UsernameAlreadyExistsException();

        // Check if name is forbidden (Like 'fuck', 'admin', ...)
        if (Config.FORBIDDEN_NAMES.length > 1) {
            for (String st : Config.FORBIDDEN_NAMES) {
                if (name.toLowerCase().contains(st.toLowerCase())) {
                    logger.info("Player creation failed for account [ " + account.getUsername() + " ] : Forbidden name.");
                    throw new ForbiddenNameException();
                }
            }
        }

        // Create player
        final Player player = playerService.create(account, name);
        if(player == null) throw new PlayerCreationFailedException();

        logger.info("Player creation success : "+ player.getName() +".");

        return player;
    }

    /**
     * @param account the account we want the players
     * @return the list of Player of the given account
     */
    public List<Player> getAllPlayersForAccount(Account account) {
        return playerService.findByAccount(account);
    }
}
