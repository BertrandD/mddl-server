package com.middlewar.api.dao;

import com.middlewar.core.model.social.FriendRequest;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestDao extends DefaultRepository<FriendRequest, String> {
    List<FriendRequest> findByRequestedId(String id);
}
