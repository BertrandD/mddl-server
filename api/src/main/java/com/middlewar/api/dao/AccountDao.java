package com.middlewar.api.dao;

import com.middlewar.core.model.Account;

/**
 * @author LEBOC Philippe
 */
public interface AccountDao extends DefaultRepository<Account, String> {
    Account findByUsername(String username);
    Account findByToken(String token);
}