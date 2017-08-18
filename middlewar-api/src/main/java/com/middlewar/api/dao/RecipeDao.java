package com.middlewar.api.dao;

import com.middlewar.core.model.instances.RecipeInstance;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Leboc Philippe.
 */
public interface RecipeDao extends JpaRepository<RecipeInstance, Long> {
}
