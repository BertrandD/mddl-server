package com.middlewar.api.services;

import com.middlewar.core.annotations.Password;
import com.middlewar.core.model.Account;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Leboc Philippe.
 */
public interface AccountService extends CrudService<Account, Integer> {
    Account findByUsername(@NotEmpty String username);
    Account create(@NotEmpty String username, @Password String password);
}
