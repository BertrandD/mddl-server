package com.middlewar.api.dao;

import com.middlewar.core.model.space.AstralObject;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Leboc Philippe.
 */
public interface AstralObjectDao extends JpaRepository<AstralObject, Long> {
    AstralObject findOneByName(String name);
}
