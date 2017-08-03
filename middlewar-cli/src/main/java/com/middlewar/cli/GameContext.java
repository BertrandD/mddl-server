package com.middlewar.cli;

import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import lombok.Data;

/**
 * @author Bertrand
 */
@Data
public class GameContext {

    protected GameContext() {}

    public static GameContext getInstance() {
        return SingletonHolder._instance;
    }

    Account account;
    Player player;

    private static class SingletonHolder {
        protected static final GameContext _instance = new GameContext();
    }
}
