package com.middlewar.api.dao;

import com.middlewar.core.model.instances.ItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Leboc Philippe.
 */
public interface ItemDao extends JpaRepository<ItemInstance, Long> {
}
