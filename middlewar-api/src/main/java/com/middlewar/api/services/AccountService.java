package com.middlewar.api.services;

import com.middlewar.core.annotations.Password;
import com.middlewar.core.model.Account;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Leboc Philippe.
 */
public interface AccountService extends CrudService<Account, Integer>, UserDetailsService {
    Account findByUsername(@NotEmpty String username);
    Account create(@NotEmpty String username, @Password String password);
}
