package com.middlewar.tests.manager;


import com.middlewar.core.exceptions.BuildingAlreadyExistsException;
import com.middlewar.core.exceptions.BuildingMaxLevelReachedException;
import com.middlewar.core.exceptions.BuildingNotFoundException;
import com.middlewar.core.exceptions.BuildingRequirementMissingException;
import com.middlewar.core.exceptions.BuildingTemplateNotFoundException;
import com.middlewar.core.exceptions.ItemNotFoundException;
import com.middlewar.core.exceptions.ItemRequirementMissingException;
import com.middlewar.core.exceptions.MaximumModulesReachedException;
import com.middlewar.core.exceptions.ModuleNotAllowedHereException;
import com.middlewar.core.exceptions.ModuleNotInInventoryException;
import com.middlewar.api.manager.impl.BaseManagerImpl;
import com.middlewar.api.manager.impl.BuildingManagerImpl;
import com.middlewar.api.manager.BuildingTaskManager;
import com.middlewar.api.manager.impl.PlanetManagerImpl;
import com.middlewar.api.manager.impl.PlayerManagerImpl;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.api.services.impl.BaseServiceImpl;
import com.middlewar.api.services.impl.BuildingServiceImpl;
import com.middlewar.api.services.impl.InventoryServiceImpl;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.space.Planet;
import com.middlewar.tests.ApplicationTest;
import com.middlewar.tests.TestUtils;
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
import java.util.concurrent.TimeUnit;

/**
 * @author Bertrand
 */
@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class BuildingManagerTest {

    @Autowired
    private BaseManagerImpl baseManager;

    @Autowired
    private BaseServiceImpl baseService;

    @Autowired
    private PlayerManagerImpl playerManager;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private PlanetManagerImpl planetManager;

    @Autowired
    private BuildingManagerImpl buildingManager;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    @Autowired
    private BuildingServiceImpl buildingService;

    private Base _base;

    @Before
    public void init() {
        WorldData.getInstance().reload();
        accountService.deleteAll();
        TestUtils.init(buildingService, inventoryService);
        MockitoAnnotations.initMocks(this);
        Account _account = accountService.create("toto", "");
        Player _player = playerManager.create(_account, "owner");
        Planet planet = planetManager.pickRandom();
        _base = baseService.create("base1", _player, planet);
    }

    @After
    public void clear() {
        buildingTaskManager.restart(true);
    }

    @Test(expected = BuildingTemplateNotFoundException.class)
    public void createShouldCheckTemplateId() {
        buildingManager.create(_base, "unknownTemplateId");
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void createShouldCheckResources() {
        buildingManager.create(_base, "shield");
    }

    @Test(expected = BuildingAlreadyExistsException.class)
    public void createShouldCheckIfBuildingMaxReached() {
        buildingManager.create(_base, "onlyone");
        buildingManager.create(_base, "onlyone");
    }

    @Test
    public void createShouldConsumeResourceAndCreateBuilding() {
        TestUtils.addResourceToBase(_base, "resource_1", 500);

        BuildingInstance building = buildingManager.create(_base, "shield");

        Assertions.assertThat(building).isNotNull();
        Assertions.assertThat(_base.getResources().get(0).getCount()).isEqualTo(400);
        Assertions.assertThat(_base.getBuildings().size()).isEqualTo(0);

        /*PriorityQueue<BuildingTask> buildQueue = baseManager.getBaseBuildingQueue(_base);
        Assertions.assertThat(buildQueue.size()).isEqualTo(1);
        BuildingTask buildingInstanceInQueue = baseManager.getBaseBuildingQueue(_base).peek();
        Assertions.assertThat(buildingInstanceInQueue.getBase()).isEqualTo(_base);
        Assertions.assertThat(buildingInstanceInQueue.getBuilding().getTemplateId()).isEqualTo("shield");
        Assertions.assertThat(buildingInstanceInQueue.getBuilding().getCurrentLevel()).isEqualTo(0);*/
    }

    @Test(expected = BuildingNotFoundException.class)
    public void getBuildingShouldCheckBuilding() {
        buildingManager.getBuilding(_base, 20);
    }

    @Test
    public void getBuildingShouldReturnBuilding() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        BuildingInstance buildingInstance1 = buildingManager.getBuilding(_base, buildingInstance.getId());
        Assertions.assertThat(buildingInstance).isEqualTo(buildingInstance1);
    }

    @Test(expected = BuildingNotFoundException.class)
    public void upgradeShouldCheckBuilding() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        buildingManager.upgrade(_base, buildingInstance.getId() + 1);
    }

    @Test(expected = BuildingMaxLevelReachedException.class)
    public void upgradeShouldCheckMaxLevel() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        buildingInstance.setCurrentLevel(buildingInstance.getTemplate().getMaxLevel());

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = BuildingMaxLevelReachedException.class)
    public void upgradeShouldCheckMaxLevelInQueue() throws InterruptedException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        buildingInstance.setCurrentLevel(buildingInstance.getTemplate().getMaxLevel() - 1);

        buildingManager.upgrade(_base, buildingInstance.getId());
        TimeUnit.SECONDS.sleep(1);
        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void upgradeShouldCheckItemRequirements() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo_req_item");

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = BuildingRequirementMissingException.class)
    public void upgradeShouldCheckBuildingRequirements() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo_req_building");

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = BuildingRequirementMissingException.class)
    public void upgradeShouldCheckBuildingRequirements2() {
        BuildingInstance silo = TestUtils.addBuildingToBase(_base, "silo", 1);
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo_req_building");

        Assertions.assertThat(silo).isNotNull();
        Assertions.assertThat(buildingInstance).isNotNull();
        Assertions.assertThat(_base.getBuildings().size()).isEqualTo(2);

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test
    public void upgradeShouldConsumeResourceAndItem() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo_req_item");
        ItemInstance item = TestUtils.addItemToBaseInventory(_base, "structure_test", 2);
        Resource resource = TestUtils.addResourceToBase(_base, "resource_1", 150);

        BuildingInstance buildingInstance1 = buildingManager.upgrade(_base, buildingInstance.getId());
        Assertions.assertThat(buildingInstance1).isNotNull();
        Assertions.assertThat(buildingInstance1).isEqualTo(buildingInstance);
        Assertions.assertThat(item.getCount()).isEqualTo(1);
        Assertions.assertThat(resource.getCount()).isEqualTo(50);
    }

    @Test(expected = BuildingNotFoundException.class)
    public void attachModuleShouldCheckIfBuildingExists() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        buildingManager.attachModule(_base, buildingInstance.getId() + 1, "module_silo_improve_1");
    }

    @Test(expected = ItemNotFoundException.class)
    public void attachModuleShouldCheckIfModuleExists() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        buildingManager.attachModule(_base, buildingInstance.getId(), "NotExistingModule");
    }

    @Test(expected = ModuleNotInInventoryException.class)
    public void attachModuleShouldCheckIfModuleIsInInventory() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_1");
    }

    @Test(expected = ModuleNotAllowedHereException.class)
    public void attachModuleShouldCheckIfModuleCanBeAttached() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        TestUtils.addItemToBaseInventory(_base, "module_silo_improve_2", 1);

        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_2");
    }

    @Test
    public void attachModuleShouldConsumeModuleAndAddItToBuilding() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        TestUtils.addItemToBaseInventory(_base, "module_silo_improve_1", 2);

        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_1");
        Assertions.assertThat(_base.getBaseInventory().getItem("module_silo_improve_1").getCount()).isEqualTo(1);
        Assertions.assertThat(buildingInstance.getModules().size()).isEqualTo(1);
        Assertions.assertThat(buildingInstance.getModules().get(0).getItemId()).isEqualTo("module_silo_improve_1");
    }

    @Test(expected = MaximumModulesReachedException.class)
    public void attachModuleShouldCheckIfMaxModuleIsReached() {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        ItemInstance itemInstance = TestUtils.addItemToBaseInventory(_base, "module_silo_improve_1", 2);

        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_1");
        Assertions.assertThat(itemInstance.getCount()).isEqualTo(1);
        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_1");
    }
}
