package com.middlewar.api.manager;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Leboc Philippe.
 */
public interface SocialActionManager {
    FriendRequest createFriendRequest(@NotNull Player player, int friendId, @NotNull @Size(max = 500) String message);
}
