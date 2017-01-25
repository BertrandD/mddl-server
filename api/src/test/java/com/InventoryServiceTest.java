package com;

import com.middlewar.api.gameserver.services.InventoryService;
import com.middlewar.api.gameserver.services.ItemContainerService;
import com.middlewar.api.gameserver.services.ItemService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.ItemContainer;
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

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private MongoOperations mongo;

    @Mock
    private ItemService itemService;

    @Mock
    private ItemContainerService itemContainerService;


    @Test
    public void testRefresh() {
        Config.load();

        // Parse
        SystemMessageData.getInstance();
        ItemData.getInstance();

        Player player = Mockito.mock(Player.class);
        Base base = new Base("Test base", player);

        ObjectStat baseStat = new ObjectStat();
        baseStat.addStat(Stats.RESOURCE_FEO);
        baseStat.add(Stats.RESOURCE_FEO, 20, StatOp.DIFF);
        base.setBaseStat(baseStat);

        ItemInstance itemInstance = new ItemInstance("resource_feo", 10);
        ItemContainer itemContainer = new ItemContainer(base, itemInstance);
        itemContainer.setStat(Stats.RESOURCE_FEO);
        itemContainer.setLastRefresh(System.currentTimeMillis() - (60 * 60 * 1000));

        inventoryService.refresh(itemContainer);

        Assertions.assertThat(itemContainer.getItem().getCount()).isEqualTo(30);

        baseStat.add(Stats.RESOURCE_FEO, Double.POSITIVE_INFINITY, StatOp.DIFF);
        itemContainer.setLastRefresh(System.currentTimeMillis() - (60 * 60 * 1000));

        inventoryService.refresh(itemContainer);

        Assertions.assertThat(itemContainer.getItem().getCount()).isEqualTo(itemContainer.getMaxVolume());
    }
}
