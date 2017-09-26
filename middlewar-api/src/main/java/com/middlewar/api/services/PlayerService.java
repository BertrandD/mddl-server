package com.middlewar.api.services;

import com.middlewar.api.dao.PlayerDAO;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.utils.Observable;
import com.middlewar.core.utils.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerService implements DefaultService<Player>, Observer {

    @Autowired
    PlayerDAO playerDAO;

    @Autowired
    BaseService baseService;

    @Autowired
    AccountService accountService;

    public Player create(Account account, String name) {

        final Player player = new Player(account, name);
        player.setId(nextId());

        account.addPlayer(player);
        account.setCurrentPlayer(player.getId());
        playerDAO.add(player);

        account.addObserver(this);
        player.addObserver(accountService);

        return player;
    }

    public Player findByName(String name) {
        return playerDAO.getByName(name);
    }

    @Override
    public void delete(Player o) {
        playerDAO.remove(o);
        clear(o);
    }

    private void clear(Player o) {
        o.setDeleted(true);
        o.notifyObservers();
    }

    public Player findOne(int currentPlayer) {
        return playerDAO.getById(currentPlayer);
    }

    @Override
    public int nextId() {
        return playerDAO.count() + 1;
    }

    public void deleteAll() {
        playerDAO.getAll().forEach(this::clear);
        playerDAO.deleteAll();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Account) {
            if (((Account) o).isDeleted()) {
                ((Account) o).getPlayers().forEach(this::delete);
            }
        } else if (o instanceof Base) {
            if (((Base) o).isDeleted()) {
                if (((Base) o).getOwner().isDeleted()) {
                    return;
                }
                ((Base) o).getOwner().getBases().remove(o);
                if (((Base) o).getOwner().getCurrentBase().equals(o)) {
                    ((Base) o).getOwner().setCurrentBase(null);
                }
            }
        }
    }
}