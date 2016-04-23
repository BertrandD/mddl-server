package com.gameserver.repository;

import com.gameserver.tasks.mongo.BuildingTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface BuildingTaskRepository extends MongoRepository<BuildingTask, String> {
    BuildingTask findFirstByBuildingOrderByEndsAtAsc(String id);
    BuildingTask findFirstByBuildingOrderByEndsAtDesc(String id);
    List<BuildingTask> findByBuilding(String id);
}
