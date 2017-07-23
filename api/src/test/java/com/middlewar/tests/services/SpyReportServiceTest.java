package com.middlewar.tests.services;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.ItemService;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.ResourceService;
import com.middlewar.api.services.SpyReportService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.vehicles.Ship;
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
public class SpyReportServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private SpyReportService spyReportService;

    @Autowired
    private AstralObjectService astralObjectService;

    private Account _account;
    private Account _account2nd;

    private Player _player;
    private Player _player2nd;

    private Base _baseTarget;
    private Base _baseSrc;

    @Before
    public void init() {
        Config.load();

        // Parse
        SystemMessageData.getInstance();
        ItemData.getInstance();

        _account = accountService.create("AccountTest", "no-password");
        _account2nd = accountService.create("AccountTest2nd", "no-password");

        _player = playerService.create(_account, "PlayerTest");
        _player2nd = playerService.create(_account2nd, "PlayerTest2nd");

        final Planet planet = (Planet) astralObjectService.create("PlanetTest", null, AstralObjectType.PLANET);
        _baseSrc = baseService.create("BaseTestSrc", _player, planet);
        _baseTarget = baseService.create("BaseTestTarget", _player2nd, planet);
    }

    @After
    public void destroy() {
        spyReportService.deleteAll();
        resourceService.deleteAll();
        itemService.deleteAll();
        baseService.deleteAll();
        playerInventoryService.deleteAll();
        playerService.deleteAll();
        accountService.deleteAll();
    }

    @Test
    public void testCreateSpyReport() {

        _baseTarget.getShips().add(new Ship(_baseTarget, "structure_test", 5));
        final Resource resource = resourceService.create(_baseTarget, "resource_1");
        resource.getItem().addCount(100);
        _baseTarget.getResources().add(resource);

        SpyReport report =  spyReportService.create(_player, _baseSrc, _baseTarget);
        Assertions.assertThat(report).isNotNull();
        Assertions.assertThat(report.getBaseSrc()).isEqualTo(_baseSrc);
        Assertions.assertThat(report.getBaseTarget()).isEqualTo(_baseTarget);
        Assertions.assertThat(report.getEntries().size()).isEqualTo(2);

        Assertions.assertThat(report.getEntries().get(ReportCategory.RESOURCES)).isNotNull();
        Assertions.assertThat(report.getEntries().get(ReportCategory.RESOURCES).get(0).getName()).isEqualTo("resource_1");
//        Assertions.assertThat(report.getEntries().get(ReportCategory.RESOURCES).get(0).getValue()).isEqualTo(100);

        Assertions.assertThat(report.getEntries().get(ReportCategory.SHIPS)).isNotNull();
        Assertions.assertThat(report.getEntries().get(ReportCategory.SHIPS).get(0).getName()).isEqualTo("structure_test");
//        Assertions.assertThat(report.getEntries().get(ReportCategory.SHIPS).get(0).getValue()).isEqualTo(5L);
    }
}
