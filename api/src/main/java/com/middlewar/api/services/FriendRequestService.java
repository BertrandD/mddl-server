package com.gameserver.services;

import com.gameserver.holders.PlayerHolder;
import com.gameserver.model.social.FriendRequest;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestService extends DefaultService<FriendRequest> {
    FriendRequest create(PlayerHolder requester, PlayerHolder requested, String message);
}
