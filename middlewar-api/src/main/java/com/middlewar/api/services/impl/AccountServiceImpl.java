package com.middlewar.api.services.impl;

import com.middlewar.api.services.AccountService;
import com.middlewar.core.annotations.Password;
import com.middlewar.core.model.Account;
import com.middlewar.core.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Slf4j
@Service
public class AccountServiceImpl extends CrudServiceImpl<Account, Integer, AccountRepository> implements UserDetailsService, AccountService {

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
        final List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        final Account account = save(new Account(username, passwordEncoder.encode(password), roles));

        log.info("New account : " + account.getUsername() + " with identifier [" + account.getId() + "]");
        return account;
    }
}