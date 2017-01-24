package com.middlewar.api.auth;

import com.middlewar.core.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByUsername(String username);
    Account findByToken(String token);
}