package com.middlewar.api.auth;

import com.middlewar.api.dao.DefaultRepository;
import com.middlewar.core.model.Account;

/**
 * @author LEBOC Philippe
 */
public interface AccountRepository extends DefaultRepository<Account, String> {
    Account findByUsername(String username);
    Account findByToken(String token);
}