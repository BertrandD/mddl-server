package com.middlewar.tests.services;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.services.*;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.holders.BaseHolder;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Leboc Philippe.
 */
@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = Application.class)
public class PlanetScanReportServiceTest {

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
    private PlanetScanReportService planetScanReportService;

    private Account _account;
    private Player _player;
    private Planet _planet;
    private Base _base;

    @Before
    public void init() {
        Config.load();

        // Parse
        SystemMessageData.getInstance();

        _account = accountService.create("AccountTest", "no-password");
        _player = playerService.create(_account, "PlayerTest");
        _planet = (Planet) astralObjectService.create("Alpha Planet", null, AstralObjectType.PLANET);
        _base = baseService.create("BaseTest", _player, _planet);
    }

    @After
    public void destroy() {
        planetScanReportService.deleteAll();
        baseService.deleteAll();
        playerInventoryService.deleteAll();
        playerService.deleteAll();
        accountService.deleteAll();
    }

    @Test
    public void testCreatePlanetScanReport() {

        _planet.addBase(_base);
        PlanetScanReport report =  planetScanReportService.create(_player, _base, _planet);

        Assertions.assertThat(report).isNotNull();
        Assertions.assertThat(report.getBaseSrc()).isEqualTo(_base);
        Assertions.assertThat(report.getEntries().size()).isEqualTo(1);

        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES)).isNotNull();
        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES).get(0).getName()).isEqualTo(_base.getName());
//        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES).get(0).getValue()).isInstanceOf(BaseHolder.class); // TODO : uncomment me !

        Assertions.assertThat(_player.getPlanetScans()).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId())).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getDate()).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getPlanet().getName()).isEqualTo(_planet.getName());
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned()).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().size()).isEqualTo(1);
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().get(_base.getId()).getName()).isEqualTo(_base.getName());
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().get(_base.getId()).getOwner().getName()).isEqualTo(_player.getName());
    }
}
