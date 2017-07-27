package com.middlewar.api.dao;

import com.middlewar.core.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface PlayerDao extends JpaRepository<Player, Long> {
    List<Player> findByAccountId(long accountId);
    Player findByName(String name);
}
