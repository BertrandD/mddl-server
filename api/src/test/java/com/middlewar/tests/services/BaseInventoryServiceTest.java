package com.middlewar.tests.services;

import com.middlewar.api.gameserver.services.BaseInventoryService;
import com.middlewar.core.model.Base;
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
public class BaseInventoryServiceTest {

    @InjectMocks
    private BaseInventoryService baseInventoryService;

    @Mock
    private MongoOperations mongo;

    @Test
    public void testCreate() {
        final Base base = new Base();

        final BaseInventory baseInventory = baseInventoryService.create(base);

        Assertions.assertThat(baseInventory).isNotNull();
        Assertions.assertThat(baseInventory.getBase()).isEqualTo(base);

    }
}
