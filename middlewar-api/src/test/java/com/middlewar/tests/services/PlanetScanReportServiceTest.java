package com.middlewar.tests.services;


import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.PlanetScanReportService;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.BaseReportEntry;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.tests.MddlTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Leboc Philippe.
 */
@MddlTest
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

    @Autowired
    private PlanetManager planetManager;

    private Account _account;
    private Player _player;
    private Planet _planet;
    private Base _base;

    @Before
    public void init() {
        Config.load();
        WorldData.getInstance().reload();
        astralObjectService.saveUniverse();

        // Parse
        SystemMessageData.getInstance();

        _account = accountService.create("AccountTest", "no-password");
        _player = playerService.create(_account, "PlayerTest");
        _planet = planetManager.pickRandom();
        _base = baseService.create("BaseTest", _player, _planet);
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
        Assertions.assertThat(report.getEntries().get(ReportCategory.BASES).get(0)).isInstanceOf(BaseReportEntry.class);

        Assertions.assertThat(_player.getPlanetScans()).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId())).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getDate()).isNotNull();
        Assertions.assertThat(_player
                .getPlanetScans()
                .get(_planet
                        .getId())
                .getPlanet()
                .getName())
                .isEqualTo(_planet
                        .getName());
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned()).isNotNull();
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().size()).isEqualTo(1);
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().get(_base.getId()).getBaseName()).isEqualTo(_base.getName());
        Assertions.assertThat(_player.getPlanetScans().get(_planet.getId()).getBaseScanned().get(_base.getId()).getBaseOwnerName()).isEqualTo(_player.getName());
    }
}
