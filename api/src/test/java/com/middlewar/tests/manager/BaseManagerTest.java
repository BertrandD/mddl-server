package com.middlewar.tests.manager;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerHasNoBaseException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.AccountManager;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import org.assertj.core.api.Assertions;
import org.junit.After;
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
    private BaseManager baseManager;

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AstralObjectService astralObjectService;

    private Account _account;
    private Player _playerOwner;
    private Player _playerNotOwner;
    private Base _base;
    private Base _base2;

    @Before
    public void init() throws NoPlayerConnectedException, PlayerNotFoundException, MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException {
        MockitoAnnotations.initMocks(this);
        _account = accountService.create("toto", "");
        _playerOwner = playerManager.createForAccount(_account, "owner");
        _playerNotOwner = playerManager.createForAccount(_account, "notOwner");
        Planet planet = (Planet) astralObjectService.create("P1", null, AstralObjectType.PLANET);
        _base = baseService.create("base1", _playerOwner, planet);
        _base2 = baseService.create("base2", _playerOwner, planet);
    }


    @After
    public void destroy() {
        accountService.deleteAll();
        baseService.deleteAll();
        playerService.deleteAll();
        astralObjectService.deleteAll();
    }

    @Test
    public void shouldReturnAllBases() throws BaseNotFoundException, NoPlayerConnectedException, PlayerNotFoundException {
        final List<Base> bases = baseManager.findAllBaseOfPlayer(_playerOwner);
        Assertions.assertThat(bases).isNotNull();
        Assertions.assertThat(bases.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnBase() throws BaseNotFoundException {
        final Base base = baseManager.getBase(_base.getId());
        Assertions.assertThat(base).isNotNull();
        Assertions.assertThat(base).isEqualTo(_base);
    }

    @Test(expected = BaseNotFoundException.class)
    public void shouldThrowExceptionIfBaseNotFound() throws BaseNotFoundException {
        baseManager.getBase(456321);
    }

    @Test(expected = BaseNotOwnedException.class)
    public void shouldCheckOwner() throws BaseNotFoundException, BaseNotOwnedException {
        baseManager.getOwnedBase(_base.getId(), _playerNotOwner);
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
