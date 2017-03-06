package com.middlewar.tests.services;

import com.middlewar.api.gameserver.services.ItemService;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Leboc Philippe.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class ItemServiceTest extends MiddlewarTest {


    @InjectMocks
    private ItemService itemService;

    @Mock
    private MongoOperations mongo;

    @Test
    public void testCreate() {
        final long COUNT = 10;
        BaseInventory baseInventory = new BaseInventory();

        ItemInstance item = itemService.create(baseInventory, "resource_1", COUNT);

        Assertions.assertThat(item).isNotNull();
        Assertions.assertThat(item.getCount()).isEqualTo(COUNT);
        Assertions.assertThat(item.getInventory()).isEqualTo(baseInventory);
        Assertions.assertThat(item.getTemplateId()).isEqualTo("resource_1");
    }
}
