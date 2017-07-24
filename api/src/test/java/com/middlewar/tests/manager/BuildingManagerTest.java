package com.middlewar.tests.manager;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.exceptions.BuildingAlreadyExistsException;
import com.middlewar.api.exceptions.BuildingCreationException;
import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.BuildingTemplateNotFoundException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.BuildingTaskManager;
import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.stats.Stats;
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
public class BuildingManagerTest {

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

    @Autowired
    private BuildingManager buildingManager;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    private Account _account;
    private Player _player;
    private Base _base;

    @Before
    public void init() throws NoPlayerConnectedException, PlayerNotFoundException, MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException {
        Config.load();
        WorldData.getInstance().reload();
        astralObjectService.saveUniverse();
        MockitoAnnotations.initMocks(this);
        buildingTaskManager.restart();
        _account = accountService.create("toto", "");
        _player = playerManager.createForAccount(_account, "owner");
        Planet planet = planetManager.pickRandom();
        _base = baseService.create("base1", _player, planet);
        // TODO : tests specific data !!!
    }

    @Test(expected = BuildingTemplateNotFoundException.class)
    public void createShouldCheckTemplateId() throws BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingCreationException, BuildingRequirementMissingException {
        buildingManager.create(_base, "unknownTemplateId");
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void createShouldCheckResources() throws BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingCreationException, BuildingRequirementMissingException {
        buildingManager.create(_base, "shield");
    }

    @Test(expected = BuildingAlreadyExistsException.class)
    public void createShouldCheckIfBuildingMaxReached() throws BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingCreationException, BuildingRequirementMissingException {
        buildingManager.create(_base, "onlyone");
        buildingManager.create(_base, "onlyone");
    }

    @Test
    public void createShouldConsumeResourceAndCreateBuilding() throws BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingCreationException, BuildingRequirementMissingException {
        Resource resource1 = inventoryService.createNewResource(_base, "resource_1");
        _base.getBaseStat().add(Stats.MAX_RESOURCE_1, 1000);
        inventoryService.addResource(resource1, 500);

        BuildingInstance building = buildingManager.create(_base, "shield");

        Assertions.assertThat(building).isNotNull();
        Assertions.assertThat(_base.getResources().get(0).getItem().getCount()).isEqualTo(400);
        Assertions.assertThat(_base.getBuildings().size()).isEqualTo(0);

        List<BuildingTask> buildQueue = baseManager.getBaseBuildingQueue(_base);
        Assertions.assertThat(buildQueue.size()).isEqualTo(1);
        BuildingTask buildingInstanceInQueue = baseManager.getBaseBuildingQueue(_base).get(0);
        Assertions.assertThat(buildingInstanceInQueue.getBase()).isEqualTo(_base);
        Assertions.assertThat(buildingInstanceInQueue.getBuilding().getBuildingId()).isEqualTo("shield");
        Assertions.assertThat(buildingInstanceInQueue.getBuilding().getCurrentLevel()).isEqualTo(0);

    }
}
