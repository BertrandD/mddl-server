package com.middlewar.core.predicate;

import com.middlewar.core.model.Player;

import java.util.function.Predicate;

/**
 * @author Leboc Philippe.
 */
public final class PlayerPredicate {

    /**
     * Default constructor
     */
    private PlayerPredicate() {
        // never instance predicate class
    }

    public static Predicate<Player> hasId(final int id) {
        return player -> player != null && player.getId() == id;
    }

}
