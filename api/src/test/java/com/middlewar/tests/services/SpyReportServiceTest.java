package com.middlewar.tests.services;

import com.middlewar.api.services.impl.SpyReportServiceImpl;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.ItemContainer;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.model.vehicles.Ship;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Leboc Philippe.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class SpyReportServiceTest extends MiddlewarTest{

    @InjectMocks
    private SpyReportServiceImpl service;

    @Mock
    private MongoOperations mongo;

    @Test
    public void testCreateSpyReport() {
        final Player player = Mockito.mock(Player.class);
        final Base baseSrc = new Base();
        final Base baseTarget = new Base();
        baseTarget.getShips().add(new Ship(baseTarget, "structure_test", 5));

        baseTarget.getResources().add(new ItemContainer(baseTarget, new ItemInstance("resource_1", 100)));

        SpyReport report =  service.create(player, baseSrc, baseTarget);
        Assertions.assertThat(report).isNotNull();
        Assertions.assertThat(report.getBaseSrc()).isEqualTo(baseSrc);
        Assertions.assertThat(report.getBaseTarget()).isEqualTo(baseTarget);
        Assertions.assertThat(report.getEntries().size()).isEqualTo(2);

        Assertions.assertThat(report.getEntries().get(ReportCategory.RESOURCES)).isNotNull();
        Assertions.assertThat(report.getEntries().get(ReportCategory.RESOURCES).get(0).getName()).isEqualTo("resource_1");
        Assertions.assertThat(report.getEntries().get(ReportCategory.RESOURCES).get(0).getValue()).isEqualTo(100);

        Assertions.assertThat(report.getEntries().get(ReportCategory.SHIPS)).isNotNull();
        Assertions.assertThat(report.getEntries().get(ReportCategory.SHIPS).get(0).getName()).isEqualTo("structure_test");
        Assertions.assertThat(report.getEntries().get(ReportCategory.SHIPS).get(0).getValue()).isEqualTo(5L);
    }
}
