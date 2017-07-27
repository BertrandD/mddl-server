package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Leboc Philippe.
 */
public interface BaseDao extends JpaRepository<Base, Long> {
}
