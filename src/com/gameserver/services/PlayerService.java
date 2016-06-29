package com.gameserver.services;

import com.auth.Account;
import com.gameserver.model.Player;
import com.gameserver.model.inventory.PlayerInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerService {

    @Autowired
    private MongoOperations mongoOperations;

    public Player findOne(String id){
        final Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoOperations.findOne(query, Player.class);
    }

    public Player findOneByName(String name) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoOperations.findOne(query, Player.class);
    }

    public List<Player> findAll() {
        return mongoOperations.findAll(Player.class);
    }

    public List<Player> findByAccount(Account account) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("account").is(account));
        return mongoOperations.find(query, Player.class);
    }

    public Player create(Account account, String name) {
        final Player player = new Player(account, name);
        final PlayerInventory inventory = new PlayerInventory(player);
        player.setInventory(inventory);
        mongoOperations.insert(player);
        mongoOperations.insert(inventory);
        return player;
    }

    @Async
    public void update(Player p) {
        mongoOperations.save(p);
    }

    @Async
    public void delete(String id){
        mongoOperations.remove(new Query(Criteria.where("id").is(id)), Player.class);
    }

    public void deleteAll() {
        mongoOperations.remove(new Query(), Player.class);
    }
}
