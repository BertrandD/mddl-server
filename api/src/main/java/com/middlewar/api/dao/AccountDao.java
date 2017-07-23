package com.middlewar.api.dao;

import com.middlewar.core.model.Account;

/**
 * @author LEBOC Philippe
 */
public interface AccountDao extends DefaultRepository<Account, Long> {
    Account findByUsername(String username);
    Account findByToken(String token);
}