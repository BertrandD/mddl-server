package com.middlewar.tests.manager;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.exceptions.*;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Bertrand
 */
@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = Application.class)
public class BaseManagerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerService playerService;

    @InjectMocks
    private BaseManager baseManager;

    @Mock
    private BaseService baseService;

    @Mock
    private PlayerManager playerManager;

    private Account _account;
    private Player _playerOwner;
    private Player _playerNotOwner;
    private Base _base;
    private Base _base2;

    @Before
    public void init() throws NoPlayerConnectedException, PlayerNotFoundException {
        MockitoAnnotations.initMocks(this);
        _playerOwner = new Player(null, "owner");
        _playerOwner.setId("owner");
        _playerNotOwner = new Player(null, "toto");
        _playerNotOwner.setId("toto");
        List<Player> players = new ArrayList<>();
        players.add(_playerOwner);
        _account = new Account("tt", "", new ArrayList<>(), "tt", null, players, _playerOwner.getId(), "");
        _base = new Base("yoloo", _playerOwner, null);
        _base.setId("yoloo");
        _base2 = new Base();
        _base2.setName("yoloo2");
        _base2.setOwner(_playerOwner);
        _base2.setId("yoloo2");
        _playerOwner.setCurrentBase(_base2);
        _playerOwner.addBase(_base);
        _playerOwner.addBase(_base2);
        when(baseService.findOne("yoloo")).thenReturn(_base);
        when(baseService.findOne("yoloo2")).thenReturn(_base2);
        when(playerManager.getCurrentPlayerForAccount(_account)).thenReturn(_playerOwner);
    }

    @Test
    public void shouldReturnAllBases() throws BaseNotFoundException, NoPlayerConnectedException, PlayerNotFoundException {
        final List<Base> bases = baseManager.findAllBaseOfPlayer(playerManager.getCurrentPlayerForAccount(_account));
        Assertions.assertThat(bases).isNotNull();
        Assertions.assertThat(bases.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnBase() throws BaseNotFoundException {
        final Base base = baseManager.getBase("yoloo");
        Assertions.assertThat(base).isNotNull();
        Assertions.assertThat(base).isEqualTo(_base);
    }

    @Test(expected = BaseNotFoundException.class)
    public void shouldThrowExceptionIfBaseNotFound() throws BaseNotFoundException {
        baseManager.getBase("yoloo2017");
    }

    @Test(expected = BaseNotOwnedException.class)
    public void shouldCheckOwner() throws BaseNotFoundException, BaseNotOwnedException {
        baseManager.getOwnedBase("yoloo", _playerNotOwner);
    }

    @Test
    public void shouldReturnCurrentBase() throws BaseNotFoundException, BaseNotOwnedException, PlayerHasNoBaseException {
        _playerOwner.setCurrentBase(_base);
        Base base = baseManager.getCurrentBaseOfPlayer(_playerOwner);
        Assertions.assertThat(base).isNotNull();
        Assertions.assertThat(base).isEqualTo(_base);

        _playerOwner.setCurrentBase(_base2);
        base = baseManager.getCurrentBaseOfPlayer(_playerOwner);
        Assertions.assertThat(base).isNotNull();
        Assertions.assertThat(base).isEqualTo(_base2);
    }
}
