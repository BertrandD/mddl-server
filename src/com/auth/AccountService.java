package com.auth;

import com.gameserver.enums.Lang;
import com.util.slack.Slack;
import com.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    private MongoOperations mongoOperations;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account account = findByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException(username);
        }else{
            return account;
        }
    }

    public Account findOne(String id){
        final Query query = new Query(Criteria.where("id").is(id));
        return mongoOperations.findOne(query, Account.class);
    }

    public Account findByUsername(String username){
        final Query query = new Query(Criteria.where("Username").is(username));
        return mongoOperations.findOne(query, Account.class);
    }

    public List<Account> findAll(){
        return mongoOperations.findAll(Account.class);
    }

    public Account create(String username, String password){
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        final Account account = new Account(username, passwordEncoder.encode(password), roles, null, Lang.EN, null, null, UUID.randomUUID().toString());
        mongoOperations.insert(account);

        // TODO: check if exist before insert

        Utils.println("New account : "+username+" with role USER");
        Slack.sendInfo("New account : "+username);
        return account;
    }

    public boolean validateToken(String token) {
        return true;
    }

    public Account getUserFromToken(String token) {
        final Query query = new Query(Criteria.where("token").is(token));
        return mongoOperations.findOne(query, Account.class);
    }

    public void update(Account account) {
        mongoOperations.save(account);
    }
}