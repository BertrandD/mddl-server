package com.middlewar.core.projections;

import com.middlewar.core.model.Base;

/**
 * @author bertrand.
 */
public class BaseLight {
    private String id;
    private String name;
    private PlayerLight owner;

    public BaseLight() {}

    public BaseLight(Base base) {
        setId(base.getId());
        setName(base.getName());
        setOwner(new PlayerLight(base.getOwner()));
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

    public PlayerLight getOwner() {
        return owner;
    }

    public void setOwner(PlayerLight owner) {
        this.owner = owner;
    }
}
