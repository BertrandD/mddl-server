package com.middlewar.api.gameserver.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.inventory.ItemContainer;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.core.model.vehicles.Ship;
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
        mongoOperations.remove(new Query(), Ship.class);
        mongoOperations.remove(new Query(), SpyReport.class);
        mongoOperations.remove(new Query(), PlanetScanReport.class);
        mongoOperations.remove(new Query(), AstralObject.class);
    }
}
