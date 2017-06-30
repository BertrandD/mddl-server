package com.middlewar.tests.manager;

import com.middlewar.api.Application;
import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Bertrand
 */
@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = Application.class)
public class PlayerManagerTest {

    @InjectMocks
    private PlayerManager playerManager;

    @Mock
    private PlayerService playerService;

    private Player _player;
    private Account _account;

    @Before
    public void init() throws NoPlayerConnectedException, PlayerNotFoundException {
        _player = new Player();
        _player.setId("yoloo");
        _player.setName("yoloo");
        List<String> players = new ArrayList<>();
        players.add(_player.getId());
        _account = new Account("tt", "", new ArrayList<>(), "tt", null, players, _player.getId(), "");
        when(playerService.findOne("yoloo")).thenReturn(_player);
    }

    @Test
    public void shouldReturnPlayer() throws BaseNotFoundException, NoPlayerConnectedException, PlayerNotFoundException {
        _account.setCurrentPlayer(_player.getId());
        final Player player = playerManager.getCurrentPlayerForAccount(_account);
        Assertions.assertThat(player).isNotNull();
        Assertions.assertThat(player).isEqualTo(_player);
    }

    @Test(expected = NoPlayerConnectedException.class)
    public void shouldThrowExceptionIfNoPlayerSelected() throws NoPlayerConnectedException, PlayerNotFoundException {
        _account.setCurrentPlayer(null);
        final Player player = playerManager.getCurrentPlayerForAccount(_account);
    }

    @Test(expected = PlayerNotFoundException.class)
    public void shouldThrowExceptionIfNoPlayerNotFound() throws NoPlayerConnectedException, PlayerNotFoundException {
        when(playerService.findOne("yoloo")).thenReturn(null);
        final Player player = playerManager.getCurrentPlayerForAccount(_account);
    }
}
