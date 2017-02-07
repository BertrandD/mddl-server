package com;

import com.middlewar.api.services.BaseInventoryService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.api.services.ItemContainerService;
import com.middlewar.api.services.ItemService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.ResourceInventory;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.model.stats.Stats;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Darbon Bertrand.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class InventoryServiceTest {

    private final int INITIAL_ITEM_COUNT = 10;
    private final int PRODUCTION_RESOURCE_PER_HOUR = 20;

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private MongoOperations mongo;

    @Mock
    private ItemService itemService;

    @Mock
    private ItemContainerService itemContainerService;

    @Mock
    private BaseInventoryService baseInventoryService;

    private Base _base;
    private Player _player;

    public InventoryServiceTest() {
        Config.load();

        // Parse
        SystemMessageData.getInstance();
        ItemData.getInstance();
    }

    public void init() {
        _player = Mockito.mock(Player.class);
        _base = new Base("Test base", _player);
        ObjectStat baseStat = new ObjectStat();
        baseStat.addStat(Stats.RESOURCE_FEO);
        baseStat.add(Stats.RESOURCE_FEO, PRODUCTION_RESOURCE_PER_HOUR, StatOp.DIFF);
        _base.setBaseStat(baseStat);
    }

    public ItemInstance generateItemInstance() {
        return new ItemInstance("resource_feo", INITIAL_ITEM_COUNT);
    }

    public ResourceInventory generateItemContainer(ItemInstance itemInstance) {
        ResourceInventory resourceInventory = new ResourceInventory(_base, itemInstance);
        resourceInventory.setStat(Stats.RESOURCE_FEO);
        return resourceInventory;
    }

    public ResourceInventory generateItemContainer() {
        return generateItemContainer(generateItemInstance());
    }

    @Test
    public void testRefresh() {
        init();
        ResourceInventory resourceInventory = generateItemContainer();
        resourceInventory.setLastRefresh(System.currentTimeMillis() - (60 * 60 * 1000));

        inventoryService.refresh(resourceInventory);

        Assertions.assertThat(resourceInventory.getItem().getCount()).isEqualTo(INITIAL_ITEM_COUNT + PRODUCTION_RESOURCE_PER_HOUR);

        _base.getBaseStat().add(Stats.RESOURCE_FEO, Double.POSITIVE_INFINITY, StatOp.DIFF);
        resourceInventory.setLastRefresh(System.currentTimeMillis() - (60 * 60 * 1000));

        inventoryService.refresh(resourceInventory);

        Assertions.assertThat(resourceInventory.getItem().getCount()).isEqualTo(resourceInventory.getMaxVolume());
    }

    @Test
    public void testConsumeResource() {
        init();
        ItemInstance itemInstance = generateItemInstance();
        itemInstance.setInventory(generateItemContainer(itemInstance));

        inventoryService.consumeResource(itemInstance, INITIAL_ITEM_COUNT -2);
        Assertions.assertThat(itemInstance.getCount()).isEqualTo(2);

        inventoryService.consumeResource(itemInstance, 10);
        Assertions.assertThat(itemInstance.getCount()).isEqualTo(2);

        inventoryService.consumeResource(itemInstance, 2);
        Assertions.assertThat(itemInstance.getCount()).isEqualTo(0);
    }

    @Test
    public void testConsumeItem() {
        init();
        ItemInstance itemInstance = new ItemInstance("module_silo_improve_c", INITIAL_ITEM_COUNT);
        BaseInventory baseInventory = new BaseInventory(_base);
        itemInstance.setInventory(baseInventory);
        baseInventory.getItems().add(itemInstance);
        Assertions.assertThat(baseInventory.getItems().size()).isEqualTo(1);

        inventoryService.consumeItem(itemInstance, INITIAL_ITEM_COUNT+1);
        Assertions.assertThat(itemInstance.getCount()).isEqualTo(INITIAL_ITEM_COUNT);

        inventoryService.consumeItem(itemInstance, 1);
        Assertions.assertThat(itemInstance.getCount()).isEqualTo(INITIAL_ITEM_COUNT-1);

        inventoryService.consumeItem(itemInstance, INITIAL_ITEM_COUNT-1);
        Assertions.assertThat(itemInstance.getCount()).isEqualTo(0);
        Assertions.assertThat(baseInventory.getItems().size()).isEqualTo(0);
    }
}
