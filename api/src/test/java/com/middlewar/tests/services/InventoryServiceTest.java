package com.middlewar.tests.services;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.dao.ResourceDao;
import com.middlewar.api.services.*;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.space.Planet;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Darbon Bertrand, LEBOC Philippe
 */
@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = Application.class)
public class InventoryServiceTest {

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AstralObjectService astralObjectService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ResourceDao resourceDao;

    private Account _account;
    private Player _player;
    private Planet _planet;
    private Base _base;
    private static final String _itemTemplate = "resource_1";

    @Before
    public void init() {
        Config.load();

        // Parse
        SystemMessageData.getInstance();
        ItemData.getInstance();

        _account = accountService.create("AccountTest", "no-password");
        _player = playerService.create(_account, "PlayerTest");
        _planet = (Planet) astralObjectService.create("Alpha Planet", null, AstralObjectType.PLANET);
        _base = baseService.create("BaseTest", _player, _planet);

    }

    @After
    public void destroy() {
        resourceDao.deleteAll();
        itemService.deleteAll();
        baseService.deleteAll();
        playerInventoryService.deleteAll();
        playerService.deleteAll();
        accountService.deleteAll();
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
    public void shouldAddAndConsumeResource() {
        final long amount = 100;

        Resource resource = inventoryService.createNewResource(_base, _itemTemplate);
        Assertions.assertThat(resource).isNotNull();

        boolean result = inventoryService.addResource(resource, amount);
        Assertions.assertThat(result).isTrue();

        resource = resourceDao.findOne(resource.getId());
        Assertions.assertThat(resource).isNotNull();
        Assertions.assertThat(resource.getItem()).isNotNull();
        Assertions.assertThat(resource.getItem().getCount()).isEqualTo(amount);

        result = inventoryService.consumeResource(resource, amount / 2);
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(resource.getItem().getCount()).isEqualTo(amount / 2);
    }
}
