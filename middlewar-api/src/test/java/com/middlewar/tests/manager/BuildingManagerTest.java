package com.middlewar.tests.manager;


import com.middlewar.api.exceptions.BuildingAlreadyExistsException;
import com.middlewar.api.exceptions.BuildingCreationException;
import com.middlewar.api.exceptions.BuildingMaxLevelReachedException;
import com.middlewar.api.exceptions.BuildingNotFoundException;
import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.BuildingTemplateNotFoundException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.MaximumModulesReachedException;
import com.middlewar.api.exceptions.ModuleNotAllowedHereException;
import com.middlewar.api.exceptions.ModuleNotInInventoryException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.NotEnoughModulesException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.impl.BaseManagerImpl;
import com.middlewar.api.manager.impl.BuildingManagerImpl;
import com.middlewar.api.manager.BuildingTaskManager;
import com.middlewar.api.manager.impl.PlanetManagerImpl;
import com.middlewar.api.manager.impl.PlayerManagerImpl;
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
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.tasks.BuildingTask;
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
import java.util.List;
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
    private BaseManagerImpl baseManagerImpl;

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerManagerImpl playerManager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlanetManagerImpl planetManager;

    @Autowired
    private AstralObjectService astralObjectService;

    @Autowired
    private BuildingManagerImpl buildingManager;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    @Autowired
    private BuildingService buildingService;

    private Base _base;

    @Before
    public void init() throws NoPlayerConnectedException, PlayerNotFoundException, MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException {
        WorldData.getInstance().reload();
        TestUtils.init(buildingService, inventoryService);
        astralObjectService.saveUniverse();
        MockitoAnnotations.initMocks(this);
        Account _account = accountService.create("toto", "");
        Player _player = playerManager.createForAccount(_account, "owner");
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
        TestUtils.addResourceToBase(_base, "resource_1", 500);

        BuildingInstance building = buildingManager.create(_base, "shield");

        Assertions.assertThat(building).isNotNull();
        Assertions.assertThat(_base.getResources().get(0).getCount()).isEqualTo(400);
        Assertions.assertThat(_base.getBuildings().size()).isEqualTo(0);

        List<BuildingTask> buildQueue = baseManagerImpl.getBaseBuildingQueue(_base);
        Assertions.assertThat(buildQueue.size()).isEqualTo(1);
        BuildingTask buildingInstanceInQueue = baseManagerImpl.getBaseBuildingQueue(_base).get(0);
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
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        BuildingInstance buildingInstance1 = buildingManager.getBuilding(_base, buildingInstance.getId());
        Assertions.assertThat(buildingInstance).isEqualTo(buildingInstance1);
    }

    @Test(expected = BuildingNotFoundException.class)
    public void upgradeShouldCheckBuilding() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        buildingManager.upgrade(_base, buildingInstance.getId() + 1);
    }

    @Test(expected = BuildingMaxLevelReachedException.class)
    public void upgradeShouldCheckMaxLevel() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        buildingInstance.setCurrentLevel(buildingInstance.getTemplate().getMaxLevel());

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = BuildingMaxLevelReachedException.class)
    public void upgradeShouldCheckMaxLevelInQueue() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException, InterruptedException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        buildingInstance.setCurrentLevel(buildingInstance.getTemplate().getMaxLevel() - 1);

        buildingManager.upgrade(_base, buildingInstance.getId());
        TimeUnit.SECONDS.sleep(1);
        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void upgradeShouldCheckItemRequirements() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo_req_item");

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = BuildingRequirementMissingException.class)
    public void upgradeShouldCheckBuildingRequirements() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo_req_building");

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test(expected = BuildingRequirementMissingException.class)
    public void upgradeShouldCheckBuildingRequirements2() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        BuildingInstance silo = TestUtils.addBuildingToBase(_base, "silo", 1);
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo_req_building");

        Assertions.assertThat(silo).isNotNull();
        Assertions.assertThat(buildingInstance).isNotNull();
        Assertions.assertThat(_base.getBuildings().size()).isEqualTo(2);

        buildingManager.upgrade(_base, buildingInstance.getId());
    }

    @Test
    public void upgradeShouldConsumeResourceAndItem() throws BuildingNotFoundException, BuildingMaxLevelReachedException, ItemRequirementMissingException, BuildingRequirementMissingException {
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
    public void attachModuleShouldCheckIfBuildingExists() throws BuildingNotFoundException, ModuleNotInInventoryException, ModuleNotAllowedHereException, MaximumModulesReachedException, NotEnoughModulesException, ItemNotFoundException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        buildingManager.attachModule(_base, buildingInstance.getId() + 1, "module_silo_improve_1");
    }

    @Test(expected = ItemNotFoundException.class)
    public void attachModuleShouldCheckIfModuleExists() throws BuildingNotFoundException, ModuleNotInInventoryException, ModuleNotAllowedHereException, MaximumModulesReachedException, NotEnoughModulesException, ItemNotFoundException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        buildingManager.attachModule(_base, buildingInstance.getId(), "NotExistingModule");
    }

    @Test(expected = ModuleNotInInventoryException.class)
    public void attachModuleShouldCheckIfModuleIsInInventory() throws BuildingNotFoundException, ModuleNotInInventoryException, ModuleNotAllowedHereException, MaximumModulesReachedException, NotEnoughModulesException, ItemNotFoundException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");

        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_1");
    }

    @Test(expected = ModuleNotAllowedHereException.class)
    public void attachModuleShouldCheckIfModuleCanBeAttached() throws BuildingNotFoundException, ModuleNotInInventoryException, ModuleNotAllowedHereException, MaximumModulesReachedException, NotEnoughModulesException, ItemNotFoundException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        TestUtils.addItemToBaseInventory(_base, "module_silo_improve_2", 1);

        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_2");
    }

    @Test
    public void attachModuleShouldConsumeModuleAndAddItToBuilding() throws BuildingNotFoundException, ModuleNotInInventoryException, ModuleNotAllowedHereException, MaximumModulesReachedException, NotEnoughModulesException, ItemNotFoundException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        TestUtils.addItemToBaseInventory(_base, "module_silo_improve_1", 2);

        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_1");
        Assertions.assertThat(_base.getBaseInventory().getItem("module_silo_improve_1").getCount()).isEqualTo(1);
        Assertions.assertThat(buildingInstance.getModules().size()).isEqualTo(1);
        Assertions.assertThat(buildingInstance.getModules().get(0).getItemId()).isEqualTo("module_silo_improve_1");
    }

    @Test(expected = MaximumModulesReachedException.class)
    public void attachModuleShouldCheckIfMaxModuleIsReached() throws BuildingNotFoundException, ModuleNotInInventoryException, ModuleNotAllowedHereException, MaximumModulesReachedException, NotEnoughModulesException, ItemNotFoundException {
        BuildingInstance buildingInstance = TestUtils.addBuildingToBase(_base, "silo");
        ItemInstance itemInstance = TestUtils.addItemToBaseInventory(_base, "module_silo_improve_1", 2);

        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_1");
        Assertions.assertThat(itemInstance.getCount()).isEqualTo(1);
        buildingManager.attachModule(_base, buildingInstance.getId(), "module_silo_improve_1");
    }
}
