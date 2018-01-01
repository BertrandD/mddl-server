package com.middlewar.api.manager.impl;

import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.PlayerNotOwnedException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.impl.PlayerServiceImpl;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author Bertrand
 */
@Service
@Slf4j
public class PlayerManagerImpl implements PlayerManager {

    @Autowired
    private PlayerServiceImpl playerService;

    public Player getCurrentPlayerForAccount(Account account) {
        if (account.getCurrentPlayerId() == 0) {
            throw new NoPlayerConnectedException();
        } else {
            final Player player = playerService.find(account.getCurrentPlayerId());
            if (player == null) {
                throw new PlayerNotFoundException();
            }
            return player;
        }
    }

    public Player getPlayerOfAccount(Account account, long id) {
        Player player = account.getPlayers().stream().filter(k -> k.getId() == (id)).findFirst().orElse(null);
        if (player == null) {
            throw new PlayerNotOwnedException();
        }
        return player;
    }

    public Player createForAccount(Account account, String name) {
        Assert.notNull(name, SystemMessageId.INVALID_PARAMETERS);

        if (account.getPlayers().size() >= Config.MAX_PLAYER_IN_ACCOUNT)
            throw new MaxPlayerCreationReachedException();

        //if (playerService.findByName(name) != null) throw new UsernameAlreadyExistsException(); TODO

        // Check if name is forbidden (Like 'fuck', 'admin', ...)
        if (Config.FORBIDDEN_NAMES.length > 1) {
            for (String st : Config.FORBIDDEN_NAMES) {
                if (name.toLowerCase().contains(st.toLowerCase())) {
                    log.info("Player creation failed for account [ " + account.getUsername() + " ] : Forbidden name.");
                    throw new ForbiddenNameException();
                }
            }
        }

        // Create player
        final Player player = playerService.create(account, name);
        if (player == null) throw new PlayerCreationFailedException();

        log.info("Player creation success : " + player.getName() + ".");

        return player;
    }

    public List<Player> getAllPlayersForAccount(Account account) {
        return emptyList(); //playerService.findByAccount(account);
    }
}
