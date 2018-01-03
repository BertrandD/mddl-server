package com.middlewar.core.repository;

import com.middlewar.core.model.instances.RecipeInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LEBOC Philippe
 */
@Repository
public interface RecipeRepository extends JpaRepository<RecipeInstance, Integer> {
}
