package com;

import com.gameserver.model.Player;
import com.gameserver.services.PlayerService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leboc Philippe.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerTest {

    @InjectMocks
    private PlayerService service;

    @Mock
    private MongoOperations mongo;

    @Test
    public void testFinAllUsers() {
        final List<Player> players = new ArrayList<>();
        players.add(new Player(null, "Bertrand"));
        players.add(new Player(null, "Philippe"));

        Mockito.when(mongo.findAll(Player.class)).thenReturn(players);

        Assertions.assertThat(service.findAll()).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void testCreatePlayer() {
        final Player player = service.create(null, "Philippe");
        Assertions.assertThat(player).isNotNull();
        Assertions.assertThat(player.getId()).isNotNull();
    }
}
