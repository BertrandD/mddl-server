package com.middlewar.api.manager.impl;

import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.exception.MaxPlayerCreationReachedException;
import com.middlewar.core.exception.PlayerCreationFailedException;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @author Bertrand
 */
@Slf4j
@Service
@Validated
@Transactional
public class PlayerManagerImpl implements PlayerManager {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerService playerService;

    @Value("${app.account.max-player}")
    private int maxPlayerPerAccount;

    @Override
    public Player create(final Account account, final String name) {

        if (account.getPlayers().size() >= maxPlayerPerAccount) {
            throw new MaxPlayerCreationReachedException();
        }

        final Player player = playerService.save(new Player(account, name));

        if(player == null) {
            throw new PlayerCreationFailedException();
        }

        account.getPlayers().add(player);
        account.setCurrentPlayerId(player.getId());
        accountService.update(account);

        // TODO: Check if name is forbidden (Like 'fuck', 'admin', ...)

        log.info("Player creation success : " + player.getName() + ".");

        return player;
    }
}
