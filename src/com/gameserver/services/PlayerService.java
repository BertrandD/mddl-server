package com.gameserver.services;

import com.auth.Account;
import com.gameserver.repository.PlayerRepository;
import com.gameserver.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public Player findOne(String id){
        return playerRepository.findOne(id);
    }

    public Player findOneByName(String name) {
        return playerRepository.findByName(name);
    }

    public Collection<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player create(Account account, String name){
        Player p = new Player(account, name);
        return playerRepository.save(p);
    }

    public void update(Player p){
        playerRepository.save(p);
    }

    public void delete(String id){
        playerRepository.delete(id);
        // TODO: cascading ?
    }

    public void deleteAll(){
        playerRepository.deleteAll();
    }
}
