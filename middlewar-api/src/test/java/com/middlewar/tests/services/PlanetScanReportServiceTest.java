package com.middlewar.tests.services;


import com.middlewar.api.manager.impl.PlanetManagerImpl;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.api.services.impl.BaseServiceImpl;
import com.middlewar.api.services.impl.PlanetScanReportServiceImpl;
import com.middlewar.api.services.impl.PlayerServiceImpl;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.BaseReportEntry;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.tests.ApplicationTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PlanetScanReportServiceTest {

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private BaseServiceImpl baseService;

    @Autowired
    private PlanetScanReportServiceImpl planetScanReportService;

    @Autowired
    private PlanetManagerImpl planetManager;

    private Account _account;
    private Player _player;
    private Planet _planet;
    private Base _base;

    @Before
    public void init() {
        WorldData.getInstance().reload();
        accountService.deleteAll();

        _account = accountService.create("AccountTest", "no-password");
        _player = playerService.create(_account, "PlayerTest");
        _planet = planetManager.pickRandom();
        _base = baseService.create("BaseTest", _player, _planet);
    }

    @Test
    public void testCreatePlanetScanReport() {
/*
        _planet.addBase(_base);
        PlanetScanReport report = planetScanReportService.create(_player, _base, _planet);

        Assertions.assertThat(report).isNotNull();
        Assertions.assertThat(report.getBaseSrc()).isEqualTo(_base);
        Assertions.assertThat(report.getEntries().size()).isEqualTo(1);

        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES)).isNotNull();
        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES).get(0).getName()).isEqualTo(_base.getName());
        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES).get(0)).isInstanceOf(BaseReportEntry.class);

        Assertions.assertThat(_player.getPlanetScans()).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId())).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getDate()).isNotNull();
        Assertions.assertThat(_player
                .getPlanetScans()
                .get(_planet.getId())
                .getPlanet()
                .getName())
                .isEqualTo(_planet.getName());
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned()).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().size()).isEqualTo(1);
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().get(_base.getId()).getBaseName()).isEqualTo(_base.getName());
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().get(_base.getId()).getBaseOwnerName()).isEqualTo(_player.getName());*/
    }
}
