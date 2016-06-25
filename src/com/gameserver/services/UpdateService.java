package com.gameserver.services;

import com.gameserver.model.instances.ItemInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
        // All update will be writed here.
        Update update = new Update();
        update.unset("lastRefresh");

        mongoOperations.findAndModify(new Query(Criteria.where("lastRefresh").exists(true)), update, ItemInstance.class);
    }
}
