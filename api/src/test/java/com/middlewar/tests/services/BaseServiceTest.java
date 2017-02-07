package com.middlewar.tests.services;

import com.middlewar.api.gameserver.services.BaseService;
import com.middlewar.api.gameserver.services.PlayerService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Leboc Philippe.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class BaseServiceTest {

    @InjectMocks
    private BaseService baseService;

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private MongoOperations mongo;

    @Test
    public void testCreate() {
        final Player player = new Player(null, "Bertrand");
        final Planet planet = new Planet("P1", null);
        final Base base = baseService.create("", player, planet);

        Assertions.assertThat(base).isNotNull();
        Assertions.assertThat(base.getBaseInventory()).isNotNull();
        Assertions.assertThat(base.getBaseInventory().getBase()).isEqualTo(base);

        Assertions.assertThat(base.getPlanet()).isEqualTo(planet);
        Assertions.assertThat(base.getOwner()).isEqualTo(player);

        Assertions.assertThat(player.getBases().size()).isEqualTo(1);
        Assertions.assertThat(player.getBases().get(0)).isEqualTo(base);
        Assertions.assertThat(player.getCurrentBase()).isEqualTo(base);
    }
}
