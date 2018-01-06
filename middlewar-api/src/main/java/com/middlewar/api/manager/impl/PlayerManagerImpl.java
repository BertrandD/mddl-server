package com.middlewar.api.manager.impl;

import com.middlewar.api.services.AccountService;
import com.middlewar.core.exception.MaxPlayerCreationReachedException;
import com.middlewar.core.exception.PlayerCreationFailedException;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * @author Bertrand
 */
@Slf4j
@Service
public class PlayerManagerImpl implements PlayerManager {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerService playerService;

    @Override
    public Player create(@NotNull final Account account, @NotEmpty final String name) {

        if (account.getPlayers().size() >= Config.MAX_PLAYER_IN_ACCOUNT)
            throw new MaxPlayerCreationReachedException();

        final Player player = playerService.save(new Player(account, name));

        if(player == null) throw new PlayerCreationFailedException();

        account.getPlayers().add(player);
        account.setCurrentPlayerId(player.getId());
        accountService.update(account);

        // TODO: Check if name is forbidden (Like 'fuck', 'admin', ...)

        log.info("Player creation success : " + player.getName() + ".");

        return player;
    }
}
