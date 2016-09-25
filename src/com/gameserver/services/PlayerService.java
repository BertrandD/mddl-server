package com.gameserver.services;

import com.auth.Account;
import com.gameserver.model.Player;
import com.gameserver.model.inventory.PlayerInventory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerService extends DatabaseService<Player> {

    @Autowired
    private MongoOperations mongoOperations;

    protected PlayerService() {
        super(Player.class);
    }

    @Override
    public Player create(Object... params) {
        if(params.length != 2) return null;

        final Account account = (Account) params[0];
        final String name = (String) params[1];

        final Player player = new Player(account, name);
        final PlayerInventory inventory = new PlayerInventory(player);
        player.setInventory(inventory);

        mongoOperations.insert(player);
        mongoOperations.insert(inventory);
        return player;
    }

   public List<Player> findBy(Account account) {
       return findBy(Criteria.where("account.$id").is(new ObjectId(account.getId())));
   }
}
