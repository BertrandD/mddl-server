package com.middlewar.core.repository;

import com.middlewar.core.model.social.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LEBOC Philippe
 */
@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Integer> {
}
