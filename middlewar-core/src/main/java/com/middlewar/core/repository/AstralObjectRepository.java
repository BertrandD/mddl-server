package com.middlewar.core.repository;

import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface AstralObjectRepository extends JpaRepository<AstralObject, Long> {

    Planet findDistinctByName(String name);

}
