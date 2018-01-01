package com.middlewar.tests.services;


import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.api.services.impl.BaseServiceImpl;
import com.middlewar.api.services.impl.BuildingServiceImpl;
import com.middlewar.api.services.impl.InventoryServiceImpl;
import com.middlewar.api.services.impl.PlayerServiceImpl;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.tests.ApplicationTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Darbon Bertrand, LEBOC Philippe
 */
@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class InventoryServiceTest {

    private static final String _itemTemplate = "resource_1";
    @Autowired
    private BuildingServiceImpl buildingService;
    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private BaseServiceImpl baseService;
    @Autowired
    private InventoryServiceImpl inventoryService;
    @Autowired
    private PlanetManager planetManager;
    private Account _account;
    private Player _player;
    private Planet _planet;
    private Base _base;

    @Before
    public void init() {
        WorldData.getInstance().reload();
        accountService.deleteAll();

        _account = accountService.create("AccountTest", "no-password");
        _player = playerService.create(_account, "PlayerTest");
        _planet = planetManager.pickRandom();
        _base = baseService.create("BaseTest1", _player, _planet);

    }

    @Test
    public void shouldAddNewItemToInventory() {
        final long amount = 100;

        final ItemInstance item = inventoryService.addItem(_player.getInventory(), _itemTemplate, amount);

        Assertions.assertThat(item).isNotNull();
        Assertions.assertThat(item.getId()).isNotNull();
        Assertions.assertThat(item.getCount()).isEqualTo(amount);
        Assertions.assertThat(item.getInventory().getId()).isEqualTo(_player.getInventory().getId());
    }

    @Test
    public void shouldReturnNullWhenTryingToAddNewNonExistingItem() {
        final ItemInstance item = inventoryService.addItem(_player.getInventory(), "non-existing-template", 10);
        Assertions.assertThat(item).isNull();
    }

    @Test
    public void shouldReturnNullWhenTryingToAddNewItemWithWrongCount() {
        final long wrongAmount = 0;
        final long anotherWrongAmount = -2;

        final ItemInstance item1 = inventoryService.addItem(_player.getInventory(), _itemTemplate, wrongAmount);
        final ItemInstance item2 = inventoryService.addItem(_player.getInventory(), _itemTemplate, anotherWrongAmount);

        Assertions.assertThat(item1).isNull();
        Assertions.assertThat(item2).isNull();
    }

    @Test
    public void shouldConsumeItemAndDecreaseCountFromInventory() {
        final long amount = 100;

        final PlayerInventory inv = _player.getInventory();
        final List<ItemInstance> items = inv.getItems();

        final ItemInstance item = inventoryService.addItem(_player.getInventory(), _itemTemplate, amount);

        Assertions.assertThat(item).isNotNull();

        final boolean result = inventoryService.consumeItem(item, 50);
        Assertions.assertThat(result).isTrue();

        final ItemInstance updatedInstance = _player.getInventory().getItem(item.getTemplateId());
        Assertions.assertThat(updatedInstance).isNotNull();
        Assertions.assertThat(updatedInstance.getCount()).isEqualTo(amount - 50);
    }

    @Test
    public void shouldCheckIfEnoughResource() {
        final long amount = 100;

        final PlayerInventory inv = _player.getInventory();
        final List<ItemInstance> items = inv.getItems();

        final ItemInstance item = inventoryService.addItem(_player.getInventory(), _itemTemplate, amount);

        Assertions.assertThat(item).isNotNull();

        final boolean result = inventoryService.consumeItem(item, amount + 1);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void shouldRemoveResourceFromInventoryIfConsumeAll() {
        final long amount = 100;

        final PlayerInventory inv = _player.getInventory();
        final List<ItemInstance> items = inv.getItems();

        final ItemInstance item = inventoryService.addItem(_player.getInventory(), _itemTemplate, amount);

        Assertions.assertThat(item).isNotNull();

        Assertions.assertThat(inv.getItem(_itemTemplate)).isNotNull();
        final boolean result = inventoryService.consumeItem(item, amount);
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(inv.getItem(_itemTemplate)).isNull();
    }

    @Test
    public void shouldAddResource() {
        final long amount = 100;
        Resource resource = initResource(amount, amount + 1);

        Assertions.assertThat(resource).isNotNull();
        Assertions.assertThat(resource.getItem()).isNotNull();
        Assertions.assertThat(resource.getCount()).isEqualTo(amount);
    }

    @Test
    public void shouldAddResourceMatchingMax() {
        final long max = 1000;
        final long amount = max + 10000;
        Resource resource = initResource(amount, max);

        Assertions.assertThat(resource).isNotNull();
        Assertions.assertThat(resource.getItem()).isNotNull();
        Assertions.assertThat(resource.getCount()).isEqualTo(max);
    }

    @Test
    public void shouldAddResourceMatchingMaxWithBuilding() {
        final long max = 1000;
        final long siloCapacity = 1000;
        final long amount = max + siloCapacity + 100;

        Resource resource = inventoryService.createNewResource(_base, _itemTemplate);
        _base.getBaseStat().add(Stats.MAX_RESOURCE_1, max - Config.BASE_INITIAL_MAX_RESOURCE_STORAGE);
        BuildingInstance buildingInstance = buildingService.create(_base, "silo");
        buildingInstance.setCurrentLevel(1);
        _base.addBuilding(buildingInstance);
        Assertions.assertThat(_base.getBuildings().contains(buildingInstance)).isTrue();
        Assertions.assertThat(resource.calcAvailableCapacity()).isEqualTo(max + siloCapacity);

        boolean result = inventoryService.addResource(resource, amount);
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(resource.getCount()).isEqualTo(max + siloCapacity);
    }

    @Test
    public void shouldRefreshResource() throws InterruptedException {
        final long max = 10000;
        final long amount = 500;
        Resource resource = initResource(amount, max);
        final double prodPerSecond = 100;
        final double prodPerHour = prodPerSecond * 60 * 60;
        _base.getBaseStat().add(resource.getStat(), prodPerHour);
        Assertions.assertThat(resource.getCount()).isEqualTo(amount);
        TimeUnit.SECONDS.sleep(3);
        inventoryService.refreshResources(_base);
        Assertions.assertThat(resource.getCount()).isGreaterThanOrEqualTo(amount + 280);
        Assertions.assertThat(resource.getCount()).isLessThanOrEqualTo(amount + 310);
    }

    private Resource initResource(long amount, long max) {
        Resource resource = inventoryService.createNewResource(_base, _itemTemplate);
        _base.getBaseStat().add(Stats.MAX_RESOURCE_1, max - Config.BASE_INITIAL_MAX_RESOURCE_STORAGE);
        Assertions.assertThat(resource).isNotNull();
        Assertions.assertThat(_base.getBaseStat().getValue(Stats.MAX_RESOURCE_1)).isEqualTo(max);

        boolean result = inventoryService.addResource(resource, amount);
        Assertions.assertThat(result).isTrue();
        return resource;
    }
}
