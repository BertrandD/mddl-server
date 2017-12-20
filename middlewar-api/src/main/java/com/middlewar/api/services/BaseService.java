package com.middlewar.api.services;

import com.middlewar.api.dao.BaseDAO;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.utils.Observable;
import com.middlewar.core.utils.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService implements DefaultService<Base>, Observer {

    @Autowired
    BaseDAO baseDAO;

    @Autowired
    PlayerService playerService;

    @Override
    public void delete(Base o) {
        baseDAO.remove(o);
        clear(o);
    }

    private void clear(Base o) {
        o.getPlanet().getBases().remove(o);
        o.setDeleted(true);
        o.notifyObservers();
    }

    public Base findOne(int id) {
        return baseDAO.getById(id);
    }

    @Override
    public int nextId() {
        return baseDAO.count() + 1;
    }

    public Base create(String name, Player player, Planet planet) {

        Base base = new Base(name, player, planet);
        base.setId(nextId());

        player.addBase(base);
        player.setCurrentBase(base);
        player.addObserver(this);

        planet.addBase(base);
        baseDAO.add(base);

        base.addObserver(playerService);
        return base;
    }

    public void deleteAll() {
        baseDAO.getAll().forEach(this::clear);
        baseDAO.deleteAll();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Player) {
            if (((Player) o).isDeleted()) {
                ((Player) o).getBases().forEach(this::delete);
            }
        }
    }
}
