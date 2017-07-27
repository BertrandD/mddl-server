package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingDao extends JpaRepository<BuildingInstance, Long> {

    BuildingInstance findOneByIdAndBaseId(long id, long base);

    List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId);
}
