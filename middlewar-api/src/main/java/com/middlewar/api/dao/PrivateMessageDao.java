package com.middlewar.api.dao;

import com.middlewar.core.model.social.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Leboc Philippe.
 */
public interface PrivateMessageDao extends JpaRepository<PrivateMessage, Long> {
}
