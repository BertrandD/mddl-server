package com.middlewar.api.manager.impl;

import com.middlewar.api.exceptions.AccountAlreadyExistsException;
import com.middlewar.api.exceptions.IncorrectCredentialsException;
import com.middlewar.api.exceptions.UsernameNotFoundException;
import com.middlewar.api.manager.AccountManager;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Bertrand
 */
@Service
public class AccountManagerImpl implements AccountManager {

    @Autowired
    private AccountServiceImpl accountService;

    public Account login(String username, String password) {
        final Account account = accountService.findByUsername(username);
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (account == null) {
            throw new UsernameNotFoundException();
        }

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        return account;

    }

    public Account register(String username, String password) {
        final Account account = accountService.findByUsername(username);
        if (account != null) throw new AccountAlreadyExistsException();
        return accountService.create(username, password);
    }
}
