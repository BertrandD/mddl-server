package com.middlewar.core.repository;

import com.middlewar.core.model.instances.BuildingInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface BuildingInstanceRepository extends JpaRepository<BuildingInstance, Integer> {
}
