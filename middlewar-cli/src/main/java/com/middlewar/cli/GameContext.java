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
