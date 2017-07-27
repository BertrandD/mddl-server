package com.middlewar.api.dao;

import com.middlewar.core.model.social.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface FriendRequestDao extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByRequestedId(long id);
}
