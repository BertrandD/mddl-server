package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.inventory.ItemContainer;
import com.gameserver.model.social.FriendRequest;
import com.gameserver.model.Player;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.tasks.BuildingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class UpdateService {

    @Autowired
    private MongoOperations mongoOperations;

    /**
     * When some Models update (field changes) are executed, execute this method to update.
     */
    public void updateDatabase() {
        // USE IT FOR LIVE SERVERS

        // All update will be writed here.
        //Update update = new Update();
        //update.unset("lastRefresh");

        //mongoOperations.findAndModify(new Query(Criteria.where("lastRefresh").exists(true)), update, ItemInstance.class);
    }

    public void resetDatabase() {
        mongoOperations.remove(new Query(), ItemInstance.class);
        mongoOperations.remove(new Query(), ItemContainer.class);
        mongoOperations.remove(new Query(), PlayerInventory.class);
        mongoOperations.remove(new Query(), BaseInventory.class);
        mongoOperations.remove(new Query(), BuildingTask.class);
        mongoOperations.remove(new Query(), BuildingInstance.class);
        mongoOperations.remove(new Query(), Base.class);
        mongoOperations.remove(new Query(), FriendRequest.class);
        mongoOperations.remove(new Query(), Player.class);
    }
}
