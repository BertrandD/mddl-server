package com.middlewar.core.repository;

import com.middlewar.core.model.vehicles.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface FleetRepository extends JpaRepository<Fleet, Integer> {
}
