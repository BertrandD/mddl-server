package com.middlewar.api.dao;

import com.middlewar.core.model.vehicles.Ship;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Leboc Philippe.
 */
public interface ShipDao extends JpaRepository<Ship, Long> {
}
