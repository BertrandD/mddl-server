package com.middlewar.tests.services;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * @author Leboc Philippe.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class PlayerServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerService playerService;

    private Account account;

    @Before
    public void init() {
        Config.load();
        account = accountService.create("PlayerTest", "password");
    }

    @Test
    public void shouldReturnNewCreatedPlayer() {
        // Given
        final String name = "Rambo";

        // When
        final Player player = playerService.create(account, name);

        // Then
        Assertions.assertThat(player).isNotNull();
        Assertions.assertThat(player.getName()).isNotNull().isNotEmpty();
        Assertions.assertThat(player.getAccount()).isNotNull();
        Assertions.assertThat(player.getAccount().getId()).isEqualTo(this.account.getId());
        Assertions.assertThat(player.getId()).isEqualTo(this.account.getCurrentPlayer());
        Assertions.assertThat(player.getBases()).isNotNull().isEmpty();
        Assertions.assertThat(player.getCurrentBase()).isNull();
        Assertions.assertThat(player.getInventory()).isNotNull();
        Assertions.assertThat(player.getInventory().getItemsToMap()).isEmpty();
    }
}
