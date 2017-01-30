package com.middlewar.api.auth;

import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import com.middlewar.api.util.slack.Slack;
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
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account account = accountRepository.findByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException(username);
        }else{
            return account;
        }
    }

    public Account findOne(String id){
        return accountRepository.findOne(id);
    }

    public Account findByUsername(String username){
        return accountRepository.findByUsername(username);
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    public Account create(String username, String password){
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        Account account = new Account(username, passwordEncoder.encode(password), roles, null, Lang.EN, null, null, UUID.randomUUID().toString());
        account = accountRepository.save(account);

        if(account == null) return null;
        Slack.sendInfo("New account : "+username);
        return account;
    }

    public boolean validateToken(String token) {
        return true;
    }

    public Account getUserFromToken(String token) {
        return accountRepository.findByToken(token);
    }

    public void update(Account account) {
        accountRepository.save(account);
    }

    public void deleteAll() {
        accountRepository.deleteAll();
    }
}