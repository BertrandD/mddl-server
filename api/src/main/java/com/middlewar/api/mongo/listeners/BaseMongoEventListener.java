package com.middlewar.api.mongo.listeners;

import com.middlewar.core.model.Base;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.mapping.event.*;

/**
 * @author Leboc Philippe.
 * @see http://docs.spring.io/spring-data/mongodb/docs/1.2.0.RELEASE/reference/html/mongo.core.html#mongodb.mapping-usage.events
 * -> ctlr + f : Lifecycle Events
 */
public class BaseMongoEventListener extends AbstractMongoEventListener<Base> {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    /**
     * WARNING: This event is handled only for these action :
     *          - find
     *          - findAndRemove
     *          - findOne
     *          - getCollection
     * Don't forget this event is never called when using findAll or a custom find method !
     * @param event the called event
     */
    @Override
    public void onAfterConvert(AfterConvertEvent<Base> event) {
        // TODO: Don't touch me please. I will be removed later
        logger.error("Initalizing stats for base : "+event.getSource().getName());
        event.getSource().initializeStats();
        super.onAfterConvert(event);
    }
}