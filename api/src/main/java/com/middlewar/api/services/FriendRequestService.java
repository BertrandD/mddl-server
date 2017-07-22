package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestService extends DefaultService<FriendRequest> {
    FriendRequest create(Player requester, Player requested, String message);
    List<FriendRequest> findPlayerRequest(String playerId);
}
