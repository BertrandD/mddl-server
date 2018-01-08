package com.middlewar.api.services.impl;

import com.middlewar.api.services.PlayerService;
import com.middlewar.core.exception.PlayerNotOwnedException;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.predicate.PlayerPredicate;
import com.middlewar.core.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
@Validated
public class PlayerServiceImpl extends CrudServiceImpl<Player, Integer, PlayerRepository> implements PlayerService {

    @Override
    public List<Player> findAll(@NotNull final Account account) {
        return account.getPlayers();
    }

    @Override
    public Player find(@NotNull final Account account, final int id) {
        return account
                .getPlayers()
                .stream()
                .filter(PlayerPredicate.hasId(id))
                .findFirst()
                .orElseThrow(PlayerNotOwnedException::new);
    }
}