package com.middlewar.api.dao;

import com.middlewar.core.model.social.FriendRequest;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestDao extends DefaultRepository<FriendRequest, Long> {
    List<FriendRequest> findByRequestedId(long id);
}
