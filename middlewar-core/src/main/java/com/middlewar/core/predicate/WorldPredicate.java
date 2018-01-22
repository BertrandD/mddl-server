package com.middlewar.core.predicate;

import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.BlackHole;
import com.middlewar.core.model.space.Moon;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.Star;

import java.util.function.Predicate;

/**
 * @author Leboc Philippe.
 */
public final class WorldPredicate {

    public static final Predicate<AstralObject> isBlackHole() {
        return object -> object != null && object instanceof BlackHole;
    }

    public static final Predicate<AstralObject> isStar() {
        return object -> object != null && object instanceof Star;
    }

    public static final Predicate<AstralObject> isPlanet() {
        return object -> object != null && object instanceof Planet;
    }

    public static final Predicate<AstralObject> isMoon() {
        return object -> object != null && object instanceof Moon;
    }

    public static final Predicate<AstralObject> hasId(long id) {
        return object -> object != null && object.getId() == id;
    }
}
