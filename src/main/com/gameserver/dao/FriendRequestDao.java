package com.gameserver.dao;

import com.gameserver.model.social.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestDao extends MongoRepository<FriendRequest, String> {
}
