package com.gameserver.holders;

/**
 * @author LEBOC Philippe
 */
public class PlayerHolder {
    private String id;
    private String name;

    public PlayerHolder(String id, String name) {
        setId(id);
        setName(name);
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

    @Override
    public boolean equals(Object o){
        if(o instanceof PlayerHolder){
            final PlayerHolder player = (PlayerHolder) o;
            if(player.getId().equalsIgnoreCase(this.getId())) return true;
        }
        return false;
    }
}
