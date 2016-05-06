package com.gameserver.services;

import com.auth.Account;
import com.gameserver.repository.PlayerRepository;
import com.gameserver.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private InventoryService inventoryService;

    public Player findOne(String id){
        return playerRepository.findOne(id);
    }

    public Player findOneByName(String name) {
        return playerRepository.findByName(name);
    }

    public Collection<Player> findAll() {
        return playerRepository.findAll();
    }

    public List<Player> findByAccount(Account account){
        return playerRepository.findByAccount(account);
    }

    public Player create(Account account, String name){
        final Player player = new Player(account, name);
        inventoryService.createPlayerInventory(player);
        return playerRepository.save(player);
    }

    @Async
    public void update(Player p){
        playerRepository.save(p);
    }

    @Async
    public void delete(String id){
        playerRepository.delete(id);
    }

    public void deleteAll(){
        playerRepository.deleteAll();
    }
}
