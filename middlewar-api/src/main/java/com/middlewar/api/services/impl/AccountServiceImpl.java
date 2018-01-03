package com.middlewar.api.services.impl;

import com.middlewar.core.exceptions.AccountAlreadyExistsException;
import com.middlewar.api.services.AccountService;
import com.middlewar.core.annotations.Password;
import com.middlewar.core.model.Account;
import com.middlewar.core.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static java.util.Collections.singletonList;

/**
 * @author LEBOC Philippe
 */
@Slf4j
@Service
@Validated
public class AccountServiceImpl extends CrudServiceImpl<Account, Integer, AccountRepository> implements UserDetailsService, AccountService {

    private static final String DEFAULT_ACCOUNT_ROLE = "ROLE_USER";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        final Account account = findByUsername(username);
        if (account == null)
            throw new UsernameNotFoundException(username); // TODO UserNotFoundException instead
        return account;
    }

    @Override
    public Account findByUsername(@NotEmpty String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Account create(@NotEmpty String username, @Password String password) {

        if(repository.findByUsername(username) != null) {
            throw new AccountAlreadyExistsException();
        }

        final Account account = save(new Account(username, passwordEncoder.encode(password), singletonList(new SimpleGrantedAuthority(DEFAULT_ACCOUNT_ROLE))));
        if(account == null)
            throw new RuntimeException(); // TODO: create specific exception

        log.info("New account : " + account.getUsername() + " with identifier [" + account.getId() + "]");
        return account;
    }
}