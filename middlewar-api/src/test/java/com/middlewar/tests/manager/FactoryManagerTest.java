package com.middlewar.tests.manager;

import com.middlewar.api.exceptions.BuildingNotFoundException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemNotUnlockedException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.manager.impl.FactoryManagerImpl;
import com.middlewar.api.manager.impl.PlanetManagerImpl;
import com.middlewar.api.manager.impl.PlayerManagerImpl;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.api.services.impl.BaseServiceImpl;
import com.middlewar.api.services.impl.BuildingServiceImpl;
import com.middlewar.api.services.impl.InventoryServiceImpl;
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
import com.middlewar.tests.ApplicationTest;
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
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class FactoryManagerTest {

    @Autowired
    private FactoryManagerImpl factoryManager;

    @Autowired
    private BaseServiceImpl baseService;

    @Autowired
    private PlayerManagerImpl playerManager;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private PlanetManagerImpl planetManager;

    @Autowired
    private BuildingServiceImpl buildingService;

    @Autowired
    private InventoryServiceImpl inventoryService;

    private Player _playerOwner;
    private Base _base;
    private BuildingInstance _moduleFactory;
    private BuildingInstance _structureFactory;

    @Before
    public void init() {
        WorldData.getInstance().reload();
        accountService.deleteAll();
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
    public void createModuleShouldCheckIfModuleExists() {
        factoryManager.createModule(_base, _moduleFactory.getId(), "NotExistingModule");
    }

    @Test(expected = BuildingNotFoundException.class)
    public void createModuleShouldCheckIfFactoryExists() {
        factoryManager.createModule(_base, 123, "module_silo_improve_1");
    }

    @Test(expected = BuildingNotFoundException.class)
    public void createModuleShouldCheckIfFactoryIsModuleFactory() {
        factoryManager.createModule(_base, _structureFactory.getId(), "module_silo_improve_1");
    }

    /*---------------------------
        Structures
     ---------------------------*/

    @Test(expected = ItemNotFoundException.class)
    public void creatStructureShouldCheckIfStructureExists() {
        factoryManager.createStructure(_base, _structureFactory.getId(), "NotExistingStructure");
    }

    @Test(expected = BuildingNotFoundException.class)
    public void createStructureShouldCheckIfFactoryExists() {
        factoryManager.createStructure(_base, 123, "structure_test");
    }

    @Test(expected = BuildingNotFoundException.class)
    public void createStructureShouldCheckIfFactoryIsStructureFactory() {
        factoryManager.createStructure(_base, _moduleFactory.getId(), "structure_test");
    }

    /*---------------------------
        Common
     ---------------------------*/

    @Test(expected = ItemNotUnlockedException.class)
    public void createItemShouldCheckIfItemIsUnlocked() {
        factoryManager.createStructure(_base, _structureFactory.getId(), "structure_test");
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void createItemShouldCheckIfHasRequirements() {
        _structureFactory.setCurrentLevel(2);
        factoryManager.createStructure(_base, _structureFactory.getId(), "structure_test");
    }

    @Test
    public void createItemShouldConsumeResourceAndReturnItem() {
        _structureFactory.setCurrentLevel(2);
        Resource resource1 = inventoryService.createNewResource(_base, "resource_1");
        _base.getBaseStat().add(Stats.MAX_RESOURCE_1, 1000 - Config.BASE_INITIAL_MAX_RESOURCE_STORAGE);
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
