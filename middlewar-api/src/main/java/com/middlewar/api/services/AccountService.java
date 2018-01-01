package com.middlewar.api.services;

import com.middlewar.api.dao.AccountDAO;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.utils.Observable;
import com.middlewar.core.utils.Observer;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AccountService implements UserDetailsService, DefaultService<Account>, Observer {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    BaseService baseService;
    @Autowired
    PlayerService playerService;
    @Autowired
    PrivateMessageService privateMessageService;


    @Override
    public UserDetails loadUserByUsername(String username) {
        final Account account = findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return account;
        }
    }

    @Override
    public void delete(Account o) {
        accountDAO.remove(o);
        clear(o);
    }

    private void clear(Account o) {
        o.setDeleted(true);
        o.setEnabled(false);
        o.setAccountNonLocked(false);
        o.setAccountNonExpired(false);
        o.setCredentialsNonExpired(false);
    }

    public Account findOne(int id) {
        return accountDAO.getById(id);
    }

    @Override
    public int nextId() {
        return accountDAO.count() + 1;
    }

    public Account findByUsername(String username) {
        return accountDAO.findByUsername(username);
    }

    public Account create(String username, String password) {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        Account account = new Account(username, passwordEncoder.encode(password), roles, Lang.EN, UUID.randomUUID().toString());
        account.setId(nextId());
        accountDAO.add(account);

        account.setEnabled(true);
        account.setAccountNonLocked(true);
        account.setAccountNonExpired(true);
        account.setCredentialsNonExpired(true);
        log.info("New account : " + username);
        // Slack.sendInfo("New account : "+username); // TODO: Add AccountServiceTestImpl
        return account;
    }

    public boolean validateToken(String token) {
        return true;
    }

    public Account getUserFromToken(String token) {
        return accountDAO.getUserFromToken(token);
    }

    public void deleteAll() {
        accountDAO.getAll().forEach(this::clear);
        accountDAO.deleteAll();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Player) {
            if (((Player) o).isDeleted()) {
                if (((Player) o).getAccount().isDeleted()) {
                    return;
                }
                ((Player) o).getAccount().getPlayers().remove(o);
                if (((Player) o).getAccount().getCurrentPlayer() == ((Player) o).getId()) {
                    ((Player) o).getAccount().setCurrentPlayer(0);
                }
            }
        }
    }
}