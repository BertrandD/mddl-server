package com.middlewar.tests.manager;


import com.middlewar.api.exceptions.BaseCreationException;
import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerHasNoBaseException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import com.middlewar.tests.ApplicationTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
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

    private Account _account;
    private Player _playerOwner;
    private Player _playerNotOwner;
    private Base _base;
    private Base _base2;

    @Before
    public void init() {
        WorldData.getInstance().reload();
        accountService.deleteAll();
        _account = accountService.create("toto", "");
        _playerOwner = playerManager.createForAccount(_account, "owner");
        _playerNotOwner = playerManager.createForAccount(_account, "notOwner");
        Planet planet = planetManager.pickRandom();
        _base = baseService.create("base1", _playerOwner, planet);
        _base2 = baseService.create("base2", _playerOwner, planet);
    }

    @Test
    public void shouldReturnAllBases() {
        final List<Base> bases = baseManager.findAllBaseOfPlayer(_playerOwner);
        Assertions.assertThat(bases).isNotNull();
        Assertions.assertThat(bases.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnBase() {
        final Base base = baseManager.getBase(_base.getId());
        Assertions.assertThat(base).isNotNull();
        Assertions.assertThat(base).isEqualTo(_base);
    }

    @Test(expected = BaseNotFoundException.class)
    public void shouldThrowExceptionIfBaseNotFound() {
        baseManager.getBase(456321);
    }

    @Test(expected = BaseNotOwnedException.class)
    public void shouldCheckOwner() {
        baseManager.getOwnedBase(_base.getId(), _playerNotOwner);
    }

    @Test
    public void shouldReturnCurrentBase() {
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
    public void shouldCheckCurrentBase() {
        _playerOwner.setCurrentBase(null);
        baseManager.getCurrentBaseOfPlayer(_playerOwner);
    }

    @Test
    @Ignore
    public void shouldReturnBaseWithBuildingQueu() {
        /*Response<Base> res = baseManager.getBaseWithBuildingQueue(_playerOwner, _base.getId());

        Assertions.assertThat(res.getPayload()).isNotNull();
        Assertions.assertThat(res.getPayload()).isInstanceOf(Base.class);
        Assertions.assertThat(res.getPayload().getId()).isEqualTo(_base.getId());
        Assertions.assertThat(res.getMeta().containsKey("queue")).isTrue();
        Assertions.assertThat(res.getMeta().get("queue")).isInstanceOf(List.class);*/
    }

    @Test
    public void shouldReturnCreatedBase() {
        Base base = baseManager.create(_playerOwner, "newBase");

        Assertions.assertThat(base.getName()).isEqualTo("newBase");
        Assertions.assertThat(base.getOwner()).isEqualTo(_playerOwner);
    }

    @Test(expected = BaseNotOwnedException.class)
    public void shouldCheckOwner2() {
        baseManager.getBuildableBuildingsOfBase(_playerNotOwner, _base.getId());

    }

    @Test
    public void shouldReturnBuildableBuildings() {
        List<BuildingHolder> buildings = baseManager.getBuildableBuildingsOfBase(_playerOwner, _base.getId());

        Assertions.assertThat(buildings).isNotNull();
        Assertions.assertThat(buildings.size()).isGreaterThan(0);
    }
}
