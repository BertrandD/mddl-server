package com.middlewar.tests.manager;

import com.middlewar.api.exceptions.BuildingAlreadyExistsException;
import com.middlewar.api.exceptions.BuildingCreationException;
import com.middlewar.api.exceptions.BuildingMaxLevelReachedException;
import com.middlewar.api.exceptions.BuildingNotFoundException;
import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.BuildingTemplateNotFoundException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.BuildingService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.utils.TimeUtil;
import com.middlewar.tests.ApplicationTest;
import com.middlewar.tests.TestUtils;
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
import java.util.concurrent.TimeUnit;

/**
 * @author Bertrand
 */
@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class BuildingTaskManagerTest {

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

    @Autowired
    private BuildingManager buildingManager;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BuildingService buildingService;

    private Base _base;

    @Before
    public void init() throws NoPlayerConnectedException, PlayerNotFoundException, MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException {
        WorldData.getInstance().reload();
        TestUtils.init(buildingService, inventoryService);
        astralObjectService.saveUniverse();
        MockitoAnnotations.initMocks(this);
        Account _account = accountService.create("tt", "");
        Player _player = playerManager.createForAccount(_account, "owner");
        Planet planet = planetManager.pickRandom();
        _base = baseService.create("base1", _player, planet);
    }

    @Test
    public void shouldQueueTask() throws BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingCreationException, BuildingRequirementMissingException, BuildingMaxLevelReachedException, BuildingNotFoundException, InterruptedException {
        BuildingInstance buildingInstance = buildingManager.create(_base, "withbuildtime");
        BuildingInstance buildingInstance2 = buildingManager.create(_base, "withbuildtime");

        Assertions.assertThat(buildingInstance.getEndsAt() - TimeUtil.getCurrentTime()).isGreaterThan(buildingInstance.getBuildTime() - 50);
        Assertions.assertThat(buildingInstance2.getEndsAt() - TimeUtil.getCurrentTime()).isGreaterThan(2 * buildingInstance.getBuildTime() - 50);
    }

    @Test
    public void buildingShouldBeInBaseAfterBuildTimeFinished() throws BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingCreationException, BuildingRequirementMissingException, BuildingMaxLevelReachedException, BuildingNotFoundException, InterruptedException {
        BuildingInstance buildingInstance = buildingManager.create(_base, "silo");
        Assertions.assertThat(buildingInstance.getCurrentLevel()).isEqualTo(0);
        Assertions.assertThat(_base.getBuildings().isEmpty()).isTrue();
        TimeUnit.SECONDS.sleep(1);
        Assertions.assertThat(_base.getBuildings().isEmpty()).isFalse();
        Assertions.assertThat(_base.getBuildings().contains(buildingInstance)).isTrue();
        Assertions.assertThat(_base.getBuildings().get(0).getCurrentLevel()).isEqualTo(1);
        buildingManager.upgrade(_base, buildingInstance.getId());
        Assertions.assertThat(_base.getBuildings().get(0).getCurrentLevel()).isEqualTo(1);
        TimeUnit.SECONDS.sleep(1);
        Assertions.assertThat(_base.getBuildings().size()).isEqualTo(1);
        Assertions.assertThat(_base.getBuildings().get(0).getCurrentLevel()).isEqualTo(2);
    }
}
