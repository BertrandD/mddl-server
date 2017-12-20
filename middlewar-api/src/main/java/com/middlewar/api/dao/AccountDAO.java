package com.middlewar.api.dao;

import com.middlewar.core.model.Account;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Service
public class AccountDAO implements DAO<Account> {
    private Vector<Account> accounts = new Vector<>();
    private Map<String, Account> accountsUsernames = new HashMap<>();
    private Map<String, Account> accountsToken = new HashMap<>();

    @Override
    public void add(Account o) {
        accounts.add(o);
        accountsUsernames.put(o.getUsername(), o);
        accountsToken.put(o.getToken(), o);
    }

    @Override
    public void remove(Account o) {
        accounts.remove(o);
        accountsUsernames.remove(o.getUsername());
        accountsToken.remove(o.getToken());
    }

    @Override
    public List<Account> getAll() {
        return accounts;
    }

    @Override
    public int count() {
        return accounts.size();
    }

    @Override
    public Account getById(int i) {
        try {
            return accounts.get(i - 1);
        } catch (Exception e) {
            return null;
        }
    }

    public Account getUserFromToken(String token) {
        return accountsToken.get(token);
    }

    public Account findByUsername(String username) {
        return accountsUsernames.get(username);
    }

    @Override
    public void deleteAll() {
        accounts.clear();
        accountsUsernames.clear();
        accountsToken.clear();
    }
}
