package com.middlewar.api.manager;

import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bertrand
 */
@Service
public class PlayerManager {
    @Autowired
    private PlayerService playerService;

    /**
     * @param account Connected account
     * @return the selected player of the current account
     * @throws NoPlayerConnectedException if the account is guest
     * @throws PlayerNotFoundException if the player of the account is not found
     */
    public Player getPlayerForAccount(Account account) throws NoPlayerConnectedException, PlayerNotFoundException {
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
}
