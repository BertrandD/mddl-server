package com.middlewar.api.manager;

import com.middlewar.core.model.Account;

/**
 * @author Leboc Philippe.
 */
public interface AccountManager {
    Account login(String username, String password);
    Account register(String username, String password);
}
