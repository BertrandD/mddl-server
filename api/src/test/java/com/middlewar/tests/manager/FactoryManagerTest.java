package com.middlewar.tests.manager;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.exceptions.BuildingNotFoundException;
import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.ItemCreationException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemNotUnlockedException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.FactoryManager;
import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.BuildingService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.stats.Stats;
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

/**
 * @author Bertrand
 */
@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = Application.class)
@Transactional
public class FactoryManagerTest {

    @Autowired
    private FactoryManager factoryManager;

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlanetManager planetManager;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private AstralObjectService astralObjectService;

    @Autowired
    private InventoryService inventoryService;

    private Player _playerOwner;
    private Base _base;
    private BuildingInstance _moduleFactory;
    private BuildingInstance _structureFactory;

    @Before
    public void init() throws MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException {
        Config.load();
        WorldData.getInstance().reload();
        astralObjectService.saveUniverse();
        MockitoAnnotations.initMocks(this);
        Account _account = accountService.create("toto", "");
        _playerOwner = playerManager.createForAccount(_account, "owner");
        Planet planet = planetManager.pickRandom();
        _base = baseService.create("base1", _playerOwner, planet);
        _moduleFactory = buildingService.create(_base, "module_factory");
        _moduleFactory.setCurrentLevel(1);
        _structureFactory = buildingService.create(_base, "structure_factory");
        _structureFactory.setCurrentLevel(1);
        _base.addBuilding(_moduleFactory);
        _base.addBuilding(_structureFactory);
    }

    /*---------------------------
        Modules
     ---------------------------*/

    @Test(expected = ItemNotFoundException.class)
    public void createModuleShouldCheckIfModuleExists() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        factoryManager.createModule(_base, _moduleFactory.getId(), "NotExistingModule");
    }

    @Test(expected = BuildingNotFoundException.class)
    public void createModuleShouldCheckIfFactoryExists() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        factoryManager.createModule(_base, 123L, "module_silo_improve_1");
    }

    @Test(expected = BuildingNotFoundException.class)
    public void createModuleShouldCheckIfFactoryIsModuleFactory() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        factoryManager.createModule(_base, _structureFactory.getId(), "module_silo_improve_1");
    }

    /*---------------------------
        Structures
     ---------------------------*/

    @Test(expected = ItemNotFoundException.class)
    public void creatStructureShouldCheckIfStructureExists() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        factoryManager.createStructure(_base, _structureFactory.getId(), "NotExistingStructure");
    }

    @Test(expected = BuildingNotFoundException.class)
    public void createStructureShouldCheckIfFactoryExists() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        factoryManager.createStructure(_base, 123L, "structure_test");
    }

    @Test(expected = BuildingNotFoundException.class)
    public void createStructureShouldCheckIfFactoryIsStructureFactory() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        factoryManager.createStructure(_base, _moduleFactory.getId(), "structure_test");
    }

    /*---------------------------
        Common
     ---------------------------*/

    @Test(expected = ItemNotUnlockedException.class)
    public void createItemShouldCheckIfItemIsUnlocked() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        factoryManager.createStructure(_base, _structureFactory.getId(), "structure_test");
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void createItemShouldCheckIfHasRequirements() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        _structureFactory.setCurrentLevel(2);
        factoryManager.createStructure(_base, _structureFactory.getId(), "structure_test");
    }

    @Test
    public void createItemShouldConsumeResourceAndReturnItem() throws ItemCreationException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException {
        _structureFactory.setCurrentLevel(2);
        Resource resource1 = inventoryService.createNewResource(_base, "resource_1");
        _base.getBaseStat().add(Stats.MAX_RESOURCE_1, 1000);
        boolean add = inventoryService.addResource(resource1, 500);
        Assertions.assertThat(add).isTrue();
        ItemInstance itemInstance = factoryManager.createStructure(_base, _structureFactory.getId(), "structure_test");
        Assertions.assertThat(resource1.getCount()).isEqualTo(499);
        Assertions.assertThat(itemInstance).isNotNull();
        Assertions.assertThat(_base.getBaseInventory().getItem("structure_test")).isNotNull();
        Assertions.assertThat(_base.getBaseInventory().getItem("structure_test").getCount()).isEqualTo(1);

        ItemInstance itemInstance2 = factoryManager.createStructure(_base, _structureFactory.getId(), "structure_test");
        Assertions.assertThat(resource1.getCount()).isEqualTo(498);
        Assertions.assertThat(itemInstance2).isNotNull();
        Assertions.assertThat(_base.getBaseInventory().getItem("structure_test")).isNotNull();
        Assertions.assertThat(_base.getBaseInventory().getItem("structure_test").getCount()).isEqualTo(2);
    }

}
