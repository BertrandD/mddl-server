package com.middlewar.api.dao;

import com.middlewar.core.model.vehicles.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Leboc Philippe.
 */
public interface FleetDao extends JpaRepository<Fleet, Long> {
}
