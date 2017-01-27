package com.middlewar.api.dao;

import com.middlewar.core.model.social.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestDao extends MongoRepository<FriendRequest, String> {
}
