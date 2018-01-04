package com.middlewar.core.predicate;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;

import java.util.function.Predicate;

/**
 * @author LEBOC Philippe
 */
public final class FriendRequestPredicate {

    /**
     * Default constructor
     */
    private FriendRequestPredicate() {
        // never instance predicate class
    }

    public static Predicate<FriendRequest> requestedIs(Player friend) {
        return friendRequest -> friendRequest.getRequested().equals(friend);
    }
}
