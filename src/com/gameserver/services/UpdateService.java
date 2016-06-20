package com.gameserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
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
    }
}
