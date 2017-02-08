package com.middlewar.core.projections;

import com.middlewar.core.model.Player;

/**
 * @author bertrand.
 */
public class PlayerLight {
    private String id;
    private String name;

    public PlayerLight() {}

    public PlayerLight(Player player) {
        setId(player.getId());
        setName(player.getName());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
