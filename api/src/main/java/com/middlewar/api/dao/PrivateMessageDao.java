package com.middlewar.api.dao;

import com.middlewar.core.model.social.PrivateMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface PrivateMessageDao extends MongoRepository<PrivateMessage, String> {
}
