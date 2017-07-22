package com.middlewar.api;

import com.middlewar.api.exceptions.*;
import com.middlewar.api.manager.AccountManager;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by bertrand on 7/4/17.
 */
@Component
public class AI {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    final String AI_NAME = "shellbash";
    final String AI_BASE_NAME = "Home";
    final String AI_PASSWD = "test";

    @PostConstruct
    public void init(){
        System.out.println("Yolooo");
        try {
            Account account = logOrRegister();
            assert account != null;
            Player player = initPlayerIfNeeded(account);
            assert player != null;
            Base base = initBaseIfNeeded(account, player);
            assert base != null;

            System.out.println("AI "+account.getUsername()+" logged in successfully !");
            System.out.println("The player is "+player.getName());
            System.out.println("The base is "+base.getName());

        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    private Account logOrRegister() {
        Account account = null;
        try {
            account = accountManager.login(AI_NAME, AI_PASSWD);
        } catch (UsernameNotFoundException e) {
            System.out.println("Account does not exists ! Let's register !");
            try {
                account = accountManager.register(AI_NAME, AI_PASSWD);
            } catch (AccountAlreadyExistsException e1) {
                e1.printStackTrace();
            }
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
        }
        return account;
    }

    private Player initPlayerIfNeeded(Account account) throws ApiException {
        Player player;
        try {
            player = playerManager.getCurrentPlayerForAccount(account);
        } catch (NoPlayerConnectedException e) {
            System.out.println("Account has no player. Let's create it !");
            player = playerManager.createForAccount(account, AI_NAME);
        }
        return player;
    }

    private Base initBaseIfNeeded(Account account, Player player) throws ApiException {
        Base base;
        try {
            base = baseManager.getCurrentBaseOfPlayer(player);
        } catch (PlayerHasNoBaseException e) {
            System.out.println("Player " + player.getName() + " has no base. Let's create it !");
            base = baseManager.create(player, AI_BASE_NAME);
        }
        return base;
    }
}
