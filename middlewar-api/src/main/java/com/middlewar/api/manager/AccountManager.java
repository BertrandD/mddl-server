package com.middlewar.api.manager;

import com.middlewar.api.exceptions.AccountAlreadyExistsException;
import com.middlewar.api.exceptions.IncorrectCredentialsException;
import com.middlewar.api.exceptions.UsernameNotFoundException;
import com.middlewar.api.services.AccountService;
import com.middlewar.core.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Bertrand
 */
@Service
@Slf4j
public class AccountManager {
    @Autowired
    private AccountService accountService;

    public Account login(String username, String password) throws UsernameNotFoundException, IncorrectCredentialsException {
        log.debug("Login attempt from " + username);
        final Account account = accountService.findByUsername(username);
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (account == null) {
            log.debug("Username not found : " + username);
            throw new UsernameNotFoundException();
        }

        if (!passwordEncoder.matches(password, account.getPassword())) {
            log.debug("Incorrect password for " + username);
            throw new IncorrectCredentialsException();
        }

        log.debug("Login success for " + username);
        return account;

    }

    public Account register(String username, String password) throws AccountAlreadyExistsException {
        log.debug("Register attempt from " + username);
        final Account account = accountService.findByUsername(username);
        if (account != null) {
            log.debug("Account already exists : " + username);
            throw new AccountAlreadyExistsException();
        }
        return accountService.create(username, password);
    }

}
