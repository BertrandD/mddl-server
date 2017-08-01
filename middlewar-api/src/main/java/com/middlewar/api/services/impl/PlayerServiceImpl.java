package com.middlewar.api.services.impl;

import com.middlewar.api.dao.PlayerDao;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.PlayerInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerServiceImpl extends DefaultServiceImpl<Player, PlayerDao> implements PlayerService {

    @Autowired
    private PlayerDao repository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Override
    public Player create(Account account, String name) {

        // Retrieve account
        final Account playerAccount = accountService.findOne(account.getId());
        if (playerAccount == null) return null;

        final Player player = repository.save(new Player(account, name));

        // Update database account
        playerAccount.addPlayer(player);
        playerAccount.setCurrentPlayer(player.getId());
        accountService.update(playerAccount);

        // update current AuthenticationPrincipal
        account.addPlayer(player);
        account.setCurrentPlayer(player.getId());

        PlayerInventory playerInventory = playerInventoryService.create(player);
        player.setInventory(playerInventory);
        repository.save(player);

        return player;
    }

    @Override
    public List<Player> findByAccount(Account account) {
        return repository.findByAccountId(account.getId());
    }

    @Override
    public Player findByName(String name) {
        return repository.findByName(name);
    }
}
