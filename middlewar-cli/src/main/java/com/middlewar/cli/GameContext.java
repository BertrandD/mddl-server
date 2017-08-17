package com.middlewar.cli;

import com.middlewar.dto.AccountDTO;
import com.middlewar.dto.BaseDTO;
import com.middlewar.dto.PlayerDTO;
import lombok.Data;

/**
 * @author Bertrand
 */
@Data
public class GameContext {

    AccountDTO account;
    PlayerDTO player;
    BaseDTO base;
    protected GameContext() {
    }

    public static GameContext getInstance() {
        return SingletonHolder._instance;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
        if (account.getPlayers().size()>0) {
            setPlayer(account.getPlayers().get(0)); // TODO : get current Player
        }
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
        setBase(player.getCurrentBase());
    }

    private static class SingletonHolder {
        protected static final GameContext _instance = new GameContext();
    }
}
