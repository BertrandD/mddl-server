package com.middlewar.api.services.impl;

import com.middlewar.api.auth.AccountService;
import com.middlewar.api.dao.PlayerDao;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerDao dao;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Override
    public Player create(Account account, String name) {

        // Retrieve account
        final Account playerAccount = accountService.findOne(account.getId());
        if(playerAccount == null) return null;

        final Player player = dao.save(new Player(account, name));

        // Update database account
        playerAccount.addPlayer(player);
        playerAccount.setCurrentPlayer(player.getId());
        accountService.update(playerAccount);

        // update current AuthenticationPrincipal
        account.addPlayer(player);
        account.setCurrentPlayer(player.getId());

        playerInventoryService.create(player);

        return player;
    }

    @Override
    public List<Player> findByAccount(Account account) {
       return dao.findByAccountId(account.getId());
    }

    @Override
    public Player findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public Player findOne(String id) {
        return dao.findOne(id);
    }

    @Override
    public List<Player> findAll() {
        return dao.findAll();
    }

    @Override
    public void update(Player player) {
        dao.save(player);
    }

    @Override
    public void remove(Player player) {
        dao.delete(player);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
