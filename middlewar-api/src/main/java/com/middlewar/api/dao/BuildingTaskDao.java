package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.tasks.BuildingTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingTaskDao extends JpaRepository<BuildingTask, Long> {
    BuildingTask findFirstByOrderByEndsAtAsc();

    List<BuildingTask> findByBuildingId(long id);

    List<BuildingTask> findByBuildingOrderByEndsAtAsc(String id);

    List<BuildingTask> findByBaseOrderByEndsAtAsc(Base base);

    BuildingTask findFirstByBuildingIdOrderByEndsAtAsc(long id);

    BuildingTask findFirstByBuildingIdOrderByEndsAtDesc(long id);
}
