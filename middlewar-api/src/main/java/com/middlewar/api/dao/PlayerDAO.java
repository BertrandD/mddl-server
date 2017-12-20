package com.middlewar.api.dao;

import com.middlewar.core.model.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Service
public class PlayerDAO implements DAO<Player> {
    private Vector<Player> players = new Vector<>();
    private Map<String, Player> playerNames = new HashMap<>();

    @Override
    public void add(Player o) {
        players.add(o);
        playerNames.put(o.getName(), o);
    }

    @Override
    public void remove(Player o) {
        playerNames.remove(o.getName());
        players.remove(o);
    }

    @Override
    public List<Player> getAll() {
        return players;
    }

    @Override
    public int count() {
        return players.size();
    }

    public Player getByName(String name) {
        return playerNames.get(name);
    }

    public Player getById(int i) {
        try {
            return players.get(i - 1);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteAll() {
        players.clear();
        playerNames.clear();
    }
}
