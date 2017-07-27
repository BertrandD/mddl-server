package com.middlewar.tests.services;

import com.middlewar.tests.ApplicationTest;
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
public class ItemServiceTest {

    @Test
    public void testCreate() {
       /* final long COUNT = 10;
        BaseInventory baseInventory = new BaseInventory();

        ItemInstance item = itemService.create(baseInventory, "resource_1", COUNT);

        Assertions.assertThat(item).isNotNull();
        Assertions.assertThat(item.getCount()).isEqualTo(COUNT);
        Assertions.assertThat(item.getInventory()).isEqualTo(baseInventory);
        Assertions.assertThat(item.getTemplateId()).isEqualTo("resource_1");*/
    }
}
