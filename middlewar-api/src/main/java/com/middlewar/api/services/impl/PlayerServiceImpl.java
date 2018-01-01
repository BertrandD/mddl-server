package com.middlewar.api.services.impl;

import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.repository.PlayerRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;


/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerServiceImpl extends CrudServiceImpl<Player, Integer, PlayerRepository> implements PlayerService {

    @Autowired
    private AccountService accountService;

    public Player create(@NotNull Account account, @NotEmpty String name) {

        final Player player = repository.save(new Player(account, name));

        if(player != null) {
            account.addPlayer(player);
            account.setCurrentPlayerId(player.getId());
            accountService.update(account);
        }

        return player;
    }
}