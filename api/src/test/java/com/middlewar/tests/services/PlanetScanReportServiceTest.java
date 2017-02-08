package com.middlewar.tests.services;

import com.middlewar.api.gameserver.services.PlanetScanReportService;
import com.middlewar.api.gameserver.services.SpyReportService;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.ItemContainer;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.PlanetScan;
import com.middlewar.core.model.vehicles.Ship;
import com.middlewar.core.projections.BaseLight;
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
public class PlanetScanReportServiceTest extends MiddlewarTest{

    @InjectMocks
    private PlanetScanReportService service;

    @Mock
    private MongoOperations mongo;

    @Test
    public void testCreatePlanetScanReport() {
        final Player player = new Player(null, "Player1");
        final Planet planet = new Planet("P1", null);
        final Base baseSrc = new Base("yoloo", player, planet);
        planet.getBases().add(baseSrc);
        PlanetScanReport report =  service.create(player, baseSrc, planet);

        Assertions.assertThat(report).isNotNull();
        Assertions.assertThat(report.getBaseSrc()).isEqualTo(baseSrc);
        Assertions.assertThat(report.getEntries().size()).isEqualTo(1);

        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES)).isNotNull();
        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES).get(0).getName()).isEqualTo(baseSrc.getName());
        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES).get(0).getValue()).isInstanceOf(BaseLight.class);

        Assertions.assertThat(player.getPlanetScans()).isNotNull();
        Assertions.assertThat(player.getPlanetScans().get(planet.getId())).isNotNull();
        Assertions.assertThat(player.getPlanetScans().get(planet.getId()).getDate()).isNotNull();
        Assertions.assertThat(player.getPlanetScans().get(planet.getId()).getPlanet().getName()).isEqualTo(planet.getName());
        Assertions.assertThat(player.getPlanetScans().get(planet.getId()).getBaseScanned()).isNotNull();
        Assertions.assertThat(player.getPlanetScans().get(planet.getId()).getBaseScanned().size()).isEqualTo(1);
        Assertions.assertThat(player.getPlanetScans().get(planet.getId()).getBaseScanned().get(baseSrc.getId()).getName()).isEqualTo(baseSrc.getName());
        Assertions.assertThat(player.getPlanetScans().get(planet.getId()).getBaseScanned().get(baseSrc.getId()).getOwner().getName()).isEqualTo(player.getName());
    }
}
