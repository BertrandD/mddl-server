package com.middlewar.api.dao;

import com.middlewar.core.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author LEBOC Philippe
 */
public interface AccountDao extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account findByToken(String token);
}