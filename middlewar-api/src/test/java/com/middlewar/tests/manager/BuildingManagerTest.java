package com.middlewar.tests.manager;


import com.middlewar.api.services.AccountService;
import com.middlewar.api.exceptions.*;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.BuildingTaskManager;
import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.BuildingService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.tests.ApplicationTest;
import com.middlewar.tests.MddlTest;
import org.assertj.core.api.Assertions;
import org.junit.After;
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
@MddlTest
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

    @Autowired
    private BuildingService buildingService;

    private Account _account;
    private Player _player;
    private Base _base;

    @Before
    public void init() throws NoPlayerConnectedException, PlayerNotFoundException, MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException {
        Config.load();
        WorldData.getInstance().reload();
        BuildingData.getInstance().load();
        astralObjectService.saveUniverse();
        MockitoAnnotations.initMocks(this);
        _account = accountService.create("toto", "");
        _player = playerManager.createForAccount(_account, "owner");
        Planet planet = planetManager.pickRandom();
        _base = baseService.create("base1", _player, planet);
    }

    @After
    public void clear() {
        buildingTaskManager.restart(true);
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
        Assertions.assertThat(_base.getResources().get(0).getCount()).isEqualTo(400);
        Assertions.assertThat(_base.getBuildings().size()).isEqualTo(0);

        List<BuildingTask> buildQueue = baseManager.getBaseBuildingQueue(_base);
        Assertions.assertThat(buildQueue.size()).isEqualTo(1);
        BuildingTask buildingInstanceInQueue = baseManager.getBaseBuildingQueue(_base).get(0);
        Assertions.assertThat(buildingInstanceInQueue.getBase()).isEqualTo(_base);
        Assertions.assertThat(buildingInstanceInQueue.getBuilding().getBuildingId()).isEqualTo("shield");
        Assertions.assertThat(buildingInstanceInQueue.getBuilding().getCurrentLevel()).isEqualTo(0);
    }

    @Test(expected = BuildingNotFoundException.class)
    public void getBuildingShouldCheckBuilding() throws BuildingNotFoundException {
        buildingManager.getBuilding(_base, 20L);
    }

    @Test
    public void getBuildingShouldReturnBuilding() throws BuildingNotFoundException {
        BuildingInstance buildingInstance = buildingService.create(_base, "silo");
        _base.addBuilding(buildingInstance);

        BuildingInstance buildingInstance1 = buildingManager.getBuilding(_base, buildingInstance.getId());
        Assertions.assertThat(buildingInstance).isEqualTo(buildingInstance1);
    }

    @Test(expected = BuildingNotFoundException.class)
    public void upgradeShouldCheckBuilding() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = buildingService.create(_base, "silo");
        _base.addBuilding(buildingInstance);

        buildingManager.upgrade(_base, buildingInstance.getId()+1);
    }

    @Test(expected = BuildingMaxLevelReachedException.class)
    public void upgradeShouldCheckMaxLevel() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = buildingService.create(_base, "silo");
        buildingInstance.setCurrentLevel(buildingInstance.getTemplate().getMaxLevel());
        _base.addBuilding(buildingInstance);

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void upgradeShouldCheckItemRequirements() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = buildingService.create(_base, "silo_req_item");
        _base.addBuilding(buildingInstance);
        Assertions.assertThat(buildingInstance).isNotNull();

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = BuildingRequirementMissingException.class)
    public void upgradeShouldCheckBuildingRequirements() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = buildingService.create(_base, "silo_req_building");
        _base.addBuilding(buildingInstance);
        Assertions.assertThat(buildingInstance).isNotNull();

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = BuildingRequirementMissingException.class)
    public void upgradeShouldCheckBuildingRequirements2() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance silo = buildingService.create(_base, "silo");
        silo.setCurrentLevel(1);
        _base.addBuilding(silo);
        BuildingInstance buildingInstance = buildingService.create(_base, "silo_req_building");
        _base.addBuilding(buildingInstance);
        Assertions.assertThat(silo).isNotNull();
        Assertions.assertThat(buildingInstance).isNotNull();
        Assertions.assertThat(_base.getBuildings().size()).isEqualTo(2);

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test
    public void upgradeShouldConsumeResource() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance silo = buildingService.create(_base, "silo");
        silo.setCurrentLevel(2);
        _base.addBuilding(silo);
        BuildingInstance buildingInstance = buildingService.create(_base, "silo_req_building");
        _base.addBuilding(buildingInstance);

        Resource resource = inventoryService.createNewResource(_base, "resource_1");
        _base.getBaseStat().add(Stats.MAX_RESOURCE_1, 1000);
        Assertions.assertThat(resource).isNotNull();
        boolean result = inventoryService.addResource(resource, 150);
        Assertions.assertThat(result).isTrue();

        BuildingInstance buildingInstance1 = buildingManager.upgrade(_base, buildingInstance.getId());
        Assertions.assertThat(buildingInstance1).isNotNull();
        Assertions.assertThat(buildingInstance1).isEqualTo(buildingInstance);
        Assertions.assertThat(resource.getCount()).isEqualTo(50);
    }

    @Test
    public void upgradeShouldConsumeItem() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = buildingService.create(_base, "silo_req_item");
        _base.addBuilding(buildingInstance);
        Assertions.assertThat(buildingInstance).isNotNull();

        _base.getBaseStat().add(Stats.BASE_MAX_STORAGE_VOLUME, 10000, StatOp.UNLOCK);
        ItemInstance item = inventoryService.addItem(_base.getBaseInventory(), "structure_test", 2);
        Assertions.assertThat(item).isNotNull();

        BuildingInstance buildingInstance1 = buildingManager.upgrade(_base, buildingInstance.getId());
        Assertions.assertThat(buildingInstance1).isNotNull();
        Assertions.assertThat(buildingInstance1).isEqualTo(buildingInstance);
        Assertions.assertThat(item.getCount()).isEqualTo(1);
    }
}
