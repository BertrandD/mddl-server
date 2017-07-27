package com.middlewar.api.dao;

import com.middlewar.core.model.inventory.Resource;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Leboc Philippe.
 */
public interface ResourceDao extends JpaRepository<Resource, Long> {
}
