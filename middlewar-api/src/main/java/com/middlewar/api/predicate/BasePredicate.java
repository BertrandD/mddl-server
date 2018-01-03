package com.middlewar.api.predicate;

import com.middlewar.core.model.Base;

import java.util.function.Predicate;

/**
 * @author LEBOC Philippe
 */
public final class BasePredicate {

    /**
     * Default constructor
     */
    private BasePredicate() {
        // Never instance Predicate class
    }

    public static Predicate<Base> hasId(final long id) {
        return base -> base != null && base.getId() == id;
    }
}
