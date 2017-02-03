package com.middlewar.api.dao;

import com.middlewar.core.model.social.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestDao extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findByRequestedId(String id);
}
