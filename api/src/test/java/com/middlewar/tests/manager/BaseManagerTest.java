package com.middlewar.tests.manager;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.exceptions.BaseCreationException;
import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerHasNoBaseException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.tasks.BuildingTask;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
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
@SpringBootTest(classes = Application.class)
@Transactional
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
    private PlanetManager planetManager;

    @Autowired
    private AstralObjectService astralObjectService;

    private Account _account;
    private Player _playerOwner;
    private Player _playerNotOwner;
    private Base _base;
    private Base _base2;

    @Before
    public void init() throws NoPlayerConnectedException, PlayerNotFoundException, MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException {
        Config.load();
        WorldData.getInstance().reload();
        astralObjectService.saveUniverse();
        MockitoAnnotations.initMocks(this);
        _account = accountService.create("toto", "");
        _playerOwner = playerManager.createForAccount(_account, "owner");
        _playerNotOwner = playerManager.createForAccount(_account, "notOwner");
        Planet planet = planetManager.pickRandom();
        _base = baseService.create("base1", _playerOwner, planet);
        _base2 = baseService.create("base2", _playerOwner, planet);
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

    @Test(expected = PlayerHasNoBaseException.class)
    public void shouldCheckCurrentBase() throws BaseNotFoundException, BaseNotOwnedException, PlayerHasNoBaseException {
        _playerOwner.setCurrentBase(null);
        baseManager.getCurrentBaseOfPlayer(_playerOwner);
    }

    @Test
    public void shouldReturnBaseWithBuildingQueu() throws BaseNotFoundException, BaseNotOwnedException, PlayerHasNoBaseException {
        Response<Base> res = baseManager.getBaseWithBuildingQueue(_playerOwner, _base.getId());

        Assertions.assertThat(res.getPayload()).isNotNull();
        Assertions.assertThat(res.getPayload()).isInstanceOf(Base.class);
        Assertions.assertThat(res.getPayload().getId()).isEqualTo(_base.getId());
        Assertions.assertThat(res.getMeta().containsKey("queue")).isTrue();
        Assertions.assertThat(res.getMeta().get("queue")).isInstanceOf(List.class);
    }

    @Test
    public void shouldReturnCreatedBase() throws BaseCreationException {
        Base base = baseManager.create(_playerOwner, "newBase");

        Assertions.assertThat(base.getName()).isEqualTo("newBase");
        Assertions.assertThat(base.getOwner()).isEqualTo(_playerOwner);
    }

    @Test(expected = BaseNotOwnedException.class)
    public void shouldCheckOwner2() throws BaseNotFoundException, BaseNotOwnedException {
        baseManager.getBuildableBuildingsOfBase(_playerNotOwner, _base.getId());

    }

    @Test
    public void shouldReturnBuildableBuildings() throws BaseNotFoundException, BaseNotOwnedException {
        List<BuildingHolder> buildings = baseManager.getBuildableBuildingsOfBase(_playerOwner, _base.getId());

        Assertions.assertThat(buildings).isNotNull();
        Assertions.assertThat(buildings.size()).isGreaterThan(0);
    }
}
