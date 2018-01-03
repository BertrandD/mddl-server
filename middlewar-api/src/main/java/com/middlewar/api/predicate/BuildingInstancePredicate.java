package com.middlewar.api.predicate;

import com.middlewar.core.model.instances.BuildingInstance;

import java.util.function.Predicate;

/**
 * @author LEBOC Philippe
 */
public final class BuildingInstancePredicate {

    /**
     * Default constructor
     */
    private BuildingInstancePredicate(){
        // Never instance Predicate class
    }

    public static Predicate<BuildingInstance> hasId(final long id) {
        return instance -> instance != null && instance.getId() == id;
    }
}
