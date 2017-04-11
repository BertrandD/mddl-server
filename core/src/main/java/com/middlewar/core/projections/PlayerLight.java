package com.middlewar.core.projections;

import com.middlewar.core.model.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bertrand.
 * TODO: delete me
 */
@Data
@NoArgsConstructor
public class PlayerLight {
    private String id;
    private String name;

    public PlayerLight(Player player) {
        setId(player.getId());
        setName(player.getName());
    }
}
