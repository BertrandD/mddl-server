package com.middlewar.core.predicate;

import com.middlewar.core.model.tasks.BuildingTask;

import java.util.function.Predicate;

/**
 * @author LEBOC Philippe
 */
public final class BuildingTaskPredicate {

    /**
     * Default constructor
     */
    private BuildingTaskPredicate() {
        // never instance predicate class
    }

    public static Predicate<BuildingTask> hasTemplateId(final String tempalteId) {
        return buildingTask -> buildingTask != null && buildingTask.getBuilding().getTemplateId().equals(tempalteId);
    }

    public static Predicate<BuildingTask> hasId(final int id) {
        return buildingTask -> buildingTask != null && buildingTask.getId() == id;
    }
}
