package com.gameserver.holders;

import com.gameserver.model.Player;

/**
 * @author LEBOC Philippe
 */
public class PlayerHolder {
    private String id;
    private String name;

    public PlayerHolder(){}

    public PlayerHolder(Player player) {
        setId(player.getId());
        setName(player.getName());
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public boolean is(Player player) {
        return player.getId().equalsIgnoreCase(this.getId());
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof PlayerHolder){
            final PlayerHolder player = (PlayerHolder) o;
            if(player.getId().equalsIgnoreCase(this.getId())) return true;
        }
        return false;
    }
}
