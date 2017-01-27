package com.gameserver.services.impl;

import com.auth.Account;
import com.gameserver.dao.PlayerDao;
import com.gameserver.model.Player;
import com.gameserver.services.PlayerService;
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

    @Override
    public Player create(Account account, String name) {
        return dao.insert(new Player(account, name));
    }

    @Override
    public List<Player> findBy(Account account) {
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
    public void clearAll() {
        dao.deleteAll();
    }
}
