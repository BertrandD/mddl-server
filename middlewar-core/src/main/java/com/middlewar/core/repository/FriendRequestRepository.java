package com.middlewar.core.repository;

import com.middlewar.core.model.social.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
}
