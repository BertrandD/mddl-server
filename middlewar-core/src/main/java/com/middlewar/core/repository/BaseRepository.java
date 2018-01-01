package com.middlewar.core.repository;

import com.middlewar.core.model.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface BaseRepository extends JpaRepository<Base, Integer> {
}
