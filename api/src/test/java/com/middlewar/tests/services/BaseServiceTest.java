package com.middlewar.tests.services;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.config.Config;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
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
public class BaseServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private AstralObjectService astralObjectService;

    private Account _account;
    private Player _player;
    private Planet _planet;

    @Before
    public void init() {
        Config.load();

        // Parse
        SystemMessageData.getInstance();
        ItemData.getInstance();

        _account = accountService.create("AccountTest", "no-password");
        _player = playerService.create(_account, "PlayerTest");
        _planet = (Planet) astralObjectService.create("Alpha Planet", null, AstralObjectType.PLANET);
    }

    @After
    public void destroy() {
        baseService.deleteAll();
        playerInventoryService.deleteAll();
        playerService.deleteAll();
        accountService.deleteAll();
    }

    @Test
    public void shouldReturnNewCreatedBase() {
        final Base base = baseService.create("BaseAeroTest", _player, _planet);

        Assertions.assertThat(base).isNotNull();
        Assertions.assertThat(base.getBaseInventory()).isNotNull();
        Assertions.assertThat(base.getBaseInventory().getBase()).isEqualTo(base);

        Assertions.assertThat(base.getPlanet()).isEqualTo(_planet);
        Assertions.assertThat(base.getOwner()).isEqualTo(_player);

        Assertions.assertThat(_planet.getBases().size()).isEqualTo(1);
        Assertions.assertThat(_planet.getBases()).contains(base);

        Assertions.assertThat(_player.getBases().size()).isEqualTo(1);
        Assertions.assertThat(_player.getBases()).contains(base);
        Assertions.assertThat(_player.getCurrentBase()).isEqualTo(base);
    }
}
