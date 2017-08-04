package com.middlewar.cli;

import com.middlewar.core.dto.AccountDTO;
import com.middlewar.core.dto.BaseDTO;
import com.middlewar.core.dto.PlayerDTO;
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

    AccountDTO account;
    PlayerDTO player;
    BaseDTO base;

    private static class SingletonHolder {
        protected static final GameContext _instance = new GameContext();
    }
}
