package com.middlewar.tests.manager;


import com.middlewar.core.exception.ForbiddenNameException;
import com.middlewar.core.exception.MaxPlayerCreationReachedException;
import com.middlewar.core.exception.NoPlayerConnectedException;
import com.middlewar.core.exception.PlayerNotFoundException;
import com.middlewar.core.exception.PlayerNotOwnedException;
import com.middlewar.core.exception.UsernameAlreadyExistsException;
import com.middlewar.api.manager.impl.PlayerManagerImpl;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.api.services.impl.PlayerServiceImpl;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.tests.ApplicationTest;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Bertrand
 */
@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class PlayerManagerTest {

    @Autowired
    private PlayerManagerImpl playerManager;

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private AccountServiceImpl accountService;

    private Player _player;
    private Player _player2;
    private Account _account;
    private Account _account2;
    private int MAX_PLAYER_IN_ACCOUNT;

    @Before
    public void init() {
        MAX_PLAYER_IN_ACCOUNT = Config.MAX_PLAYER_IN_ACCOUNT;
        accountService.deleteAll();
        _account = accountService.create("tt", "tt");
        _account2 = accountService.create("tt2", "tt");
        _player = playerService.create(_account, "yoloo");
        _player2 = playerService.create(_account2, "yoloo2");
    }

    @After
    public void reset() {
        Config.MAX_PLAYER_IN_ACCOUNT = MAX_PLAYER_IN_ACCOUNT;
    }

    @Test
    public void shouldReturnPlayer() {
        /*_account.setCurrentPlayer(_player.getId());
        final Player player = playerManager.getCurrentPlayerForAccount(_account);
        Assertions.assertThat(player).isNotNull();
        Assertions.assertThat(player).isEqualTo(_player);*/
    }

    @Test(expected = NoPlayerConnectedException.class)
    public void shouldThrowExceptionIfNoPlayerSelected() {
        /*_account.setCurrentPlayer(0);
        final Player player = playerManager.getCurrentPlayerForAccount(_account);*/
    }

    @Test(expected = PlayerNotFoundException.class)
    public void shouldThrowExceptionIfNoPlayerNotFound() {
        /*_account.setCurrentPlayer(1123546);
        final Player player = playerManager.getCurrentPlayerForAccount(_account);*/
    }

    @Test(expected = MaxPlayerCreationReachedException.class)
    public void shouldCheckMaxPlayer() {
        Config.MAX_PLAYER_IN_ACCOUNT = 1;
        playerManager.create(_account, "toto");
    }

    @Test(expected = UsernameAlreadyExistsException.class)
    public void shouldCheckExistingUsername() {
        playerManager.create(_account, "yoloo");
    }

    @Test(expected = ForbiddenNameException.class)
    public void shouldCheckForbiddentNames() {
        Config.FORBIDDEN_NAMES = "sbouf,sgni,forbiddenName".split(",");
        playerManager.create(_account, "forbiddenName");
    }

//    @Test(expected = PlayerCreationFailedException.class)
//    public void shouldThrowExceptionIfFailed() {
//        playerManager.createFriendRequest(_account, "toto");
//    }

    @Test
    public void shouldReturnCreatedPlayer() {
        final Player player3 = playerManager.create(_account, "toto3");
        Assertions.assertThat(player3).isNotNull();
        Assertions.assertThat(player3.getName()).isEqualTo("toto3");
    }

    @Test(expected = PlayerNotOwnedException.class)
    public void shouldCheckOwnerAndThrowException() {
        playerManager.getPlayerOfAccount(_account, _player2.getId());
    }

    @Test
    public void shouldCheckOwneAndReturnPlayer() {
        Player player = playerManager.getPlayerOfAccount(_account, _player.getId());
        Assertions.assertThat(player).isEqualTo(_player);
    }

    @Test
    public void shouldReturnAllPlayers() {
        List<Player> players = playerManager.findAll(_account);
        Assertions.assertThat(players.size()).isEqualTo(1);
        Assertions.assertThat(players.get(0)).isEqualTo(_player);
    }
}
