package com.middlewar.api.services;

import com.middlewar.api.dao.AccountDao;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author LEBOC Philippe
 */
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account account = accountDao.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return account;
        }
    }

    public Account findOne(Long id) {
        return accountDao.findOne(id);
    }

    public Account findByUsername(String username) {
        return accountDao.findByUsername(username);
    }

    public List<Account> findAll() {
        return accountDao.findAll();
    }

    public Account create(String username, String password) {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        Account account = new Account(username, passwordEncoder.encode(password), roles, Lang.EN, UUID.randomUUID().toString());
        account = accountDao.save(account);

        if (account == null) return null;
        // Slack.sendInfo("New account : "+username); // TODO: Add AccountServiceTestImpl
        return account;
    }

    public boolean validateToken(String token) {
        return true;
    }

    public Account getUserFromToken(String token) {
        return accountDao.findByToken(token);
    }

    public void update(Account account) {
        accountDao.save(account);
    }

    public void deleteAll() {
        accountDao.deleteAll();
    }
}