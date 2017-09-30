package com.middlewar.ai;

import com.middlewar.api.exceptions.AccountAlreadyExistsException;
import com.middlewar.api.exceptions.ApiException;
import com.middlewar.api.exceptions.IncorrectCredentialsException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerHasNoBaseException;
import com.middlewar.api.exceptions.UsernameNotFoundException;
import com.middlewar.api.manager.AccountManager;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.FriendRequestService;
import com.middlewar.api.services.PlanetScanReportService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by bertrand on 7/4/17.
 */
@Component
@Slf4j
public class AI {
    final String AI_NAME = "shellbash";
    final String AI_BASE_NAME = "Home";
    final String AI_PASSWD = "test";

    @Autowired
    private AccountManager accountManager;
    @Autowired
    private PlayerManager playerManager;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private PlanetScanReportService planetScanReportService;

    //    @PostConstruct
    public void init() {
        Config.load();
        System.out.println("Yolooo");
        try {
            Account account = logOrRegister();
            assert account != null;
            Player player = initPlayerIfNeeded(account);
            assert player != null;
            Base base = initBaseIfNeeded(account, player);
            assert base != null;

            System.out.println("AI " + account.getUsername() + " logged in successfully !");
            System.out.println("The player is " + player.getName());
            System.out.println("The base is " + base.getName());

            Player player1 = playerManager.createForAccount(account, "qldskj");
            baseManager.create(player1, "f");
            baseManager.create(player1, "f2");

//            Planet planet = WorldData.getInstance().getRandomPlanet();
//            PlanetScanReport report = planetScanReportService.create(player, base, planet);

            System.out.println("Cleaning...");
//            planetScanReportService.remove(report);
            baseService.deleteAll();
//            astralObjectService.deleteAll();
            accountService.deleteAll();
//            playerService.remove(player);
            System.out.println("Cleaning... OK");

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

    private Player initPlayerIfNeeded(Account account) {
        Player player;
        try {
            player = playerManager.getCurrentPlayerForAccount(account);
        } catch (NoPlayerConnectedException e) {
            System.out.println("Account has no player. Let's create it !");
            player = playerManager.createForAccount(account, AI_NAME);
        }
        return player;
    }

    private Base initBaseIfNeeded(Account account, Player player) {
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
