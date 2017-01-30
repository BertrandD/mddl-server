package com.middlewar.tests.services;

import com.middlewar.api.gameserver.services.PlayerService;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
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
public class PlayerServiceTest {

    @InjectMocks
    private PlayerService service;

    @Mock
    private MongoOperations mongo;

    @Test
    public void testCreatePlayer() {
        final Account account = Mockito.mock(Account.class);
        final Player player = service.create(account, "Philippe");
        Assertions.assertThat(player).isNotNull();
        Assertions.assertThat(player.getId()).isNotNull();
        Assertions.assertThat(player.getInventory()).isNotNull();
        Assertions.assertThat(player.getInventory().getPlayer()).isEqualTo(player);
    }
}
