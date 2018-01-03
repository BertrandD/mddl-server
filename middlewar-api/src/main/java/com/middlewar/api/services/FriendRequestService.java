package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestService extends CrudService<FriendRequest, Integer> {
    FriendRequest create(@NotNull Player requester, @NotNull Player requested, @NotEmpty String message);
}
