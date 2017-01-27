package com.gameserver.dao;

import com.gameserver.model.social.PrivateMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface PrivateMessageDao extends MongoRepository<PrivateMessage, String> {
}
