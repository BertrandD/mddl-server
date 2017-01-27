package com.middlewar.api.services;

import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.social.FriendRequest;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestService extends DefaultService<FriendRequest> {
    FriendRequest create(PlayerHolder requester, PlayerHolder requested, String message);
}
