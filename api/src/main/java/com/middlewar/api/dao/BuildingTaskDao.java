package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.tasks.BuildingTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingTaskDao extends MongoRepository<BuildingTask, String> {
    BuildingTask findFirstByOrderByEndsAtAsc();
    List<BuildingTask> findByBuilding(String id);
    List<BuildingTask> findByBuildingOrderByEndsAtAsc(String id);
    List<BuildingTask> findByBaseOrderByEndsAtAsc(Base base);
    BuildingTask findFirstByBuildingOrderByEndsAtAsc(String id);
    BuildingTask findFirstByBuildingOrderByEndsAtDesc(String id);
}
