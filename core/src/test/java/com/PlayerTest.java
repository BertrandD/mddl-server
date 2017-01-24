package com;

import com.middlewar.core.model.Player;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PlayerTest {

    @Test
    public void testPlayer() {
        final Player player = new Player();
        Assertions.assertThat(player).isNotNull();
    }

}
