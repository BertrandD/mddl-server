package com.middlewar.tests.services;

import com.middlewar.tests.ApplicationTest;
import com.middlewar.tests.MddlTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * @author Leboc Philippe.
 */

@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class BaseInventoryServiceTest {

    @Test
    public void testCreate() {
        /*final Base base = new Base();

        final BaseInventory baseInventory = baseInventoryService.create(base);

        Assertions.assertThat(baseInventory).isNotNull();
        Assertions.assertThat(baseInventory.getCurrentBaseOfPlayer()).isEqualTo(base);*/

    }
}
