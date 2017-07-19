package com.middlewar.api.manager;

import com.middlewar.api.auth.AccountService;
import com.middlewar.api.exceptions.AccountAlreadyExistsException;
import com.middlewar.api.exceptions.IncorrectCredentialsException;
import com.middlewar.api.exceptions.UsernameNotFoundException;
import com.middlewar.core.model.Account;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Bertrand
 */
@Service
public class AccountManager {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Autowired
    private AccountService accountService;

    public Account login(String username, String password) throws UsernameNotFoundException, IncorrectCredentialsException {
        final Account account = accountService.findByUsername(username);
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(account == null) {
            throw new UsernameNotFoundException();
        }

        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        return account;

    }

    public Account register(String username, String password) throws AccountAlreadyExistsException {
        final Account account = accountService.findByUsername(username);
        if(account != null) throw new AccountAlreadyExistsException();
        return accountService.create(username, password);
    }

}
