package com.middlewar.api.services;

import com.middlewar.api.dao.FriendRequestDao;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestService extends DefaultService<FriendRequest, FriendRequestDao> {
    FriendRequest create(Player requester, Player requested, String message);

    List<FriendRequest> findPlayerRequest(long playerId);
}
