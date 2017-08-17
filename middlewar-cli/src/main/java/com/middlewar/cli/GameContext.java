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

    public static void setInstance(GameContext gameContext) {
        SingletonHolder._instance = gameContext;
    }

    private static class SingletonHolder {
        protected static GameContext _instance = new GameContext();
    }
}
