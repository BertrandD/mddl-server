package com.gameserver.repository;

import com.gameserver.model.tasks.BuildingTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface BuildingTaskRepository extends MongoRepository<BuildingTask, String> {
    BuildingTask findFirstByOrderByEndsAtAsc();
    BuildingTask findFirstByBuildingOrderByEndsAtAsc(String id);
    BuildingTask findFirstByBuildingOrderByEndsAtDesc(String id);
    List<BuildingTask> findByBuilding(String id);
    List<BuildingTask> findByBuildingOrderByEndsAtAsc(String id);
    List<BuildingTask> findByBaseOrderByEndsAtAsc(String id);
}
