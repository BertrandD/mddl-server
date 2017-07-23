package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.tasks.BuildingTask;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingTaskService extends DefaultService<BuildingTask> {
    BuildingTask create(BuildingInstance inst, long timestamp, int level);
    BuildingTask findFirstByOrderByEndsAtAsc();
    List<BuildingTask> findByBuilding(String id);
    List<BuildingTask> findByBuildingOrderByEndsAtAsc(String id);
    List<BuildingTask> findByBaseOrderByEndsAtAsc(Base base);
    BuildingTask findFirstByBuildingOrderByEndsAtAsc(String id);
    BuildingTask findFirstByBuildingOrderByEndsAtDesc(long id);
}
