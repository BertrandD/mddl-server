package com.middlewar.core.holders;

import com.middlewar.core.model.Player;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author LEBOC Philippe
 */
@Data
public class PlayerHolder {
    @Id
    private String id;
    private String name;

    public PlayerHolder(Player player) {
        setId(player.getId());
        setName(player.getName());
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
