package com.middlewar.core.repository;

import com.middlewar.core.model.tasks.BuildingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface BuildingTaskRepository extends JpaRepository<BuildingTask, Integer> {
}
