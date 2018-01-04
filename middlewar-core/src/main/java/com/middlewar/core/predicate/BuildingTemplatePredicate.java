package com.middlewar.core.predicate;

import com.middlewar.core.model.buildings.Building;

import java.util.function.Predicate;

/**
 * @author LEBOC Philippe
 */
public final class BuildingTemplatePredicate {

    /**
     * Default constructor
     */
    private BuildingTemplatePredicate(){
        // never instance predicate class
    }

    public static Predicate<Building> hasTemplateId(String id) {
        return building -> building != null && building.getId().equals(id);
    }
}
