package com.middlewar.core.repository;

import com.middlewar.core.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
