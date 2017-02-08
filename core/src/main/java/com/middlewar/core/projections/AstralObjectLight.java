package com.middlewar.core.projections;

import com.middlewar.core.model.space.AstralObject;

/**
 * @author bertrand.
 */
public class AstralObjectLight {
    private String id;
    private String name;
    private long nbStatellites;

    public AstralObjectLight() {}

    public AstralObjectLight(AstralObject astralObject) {
        this.id = astralObject.getId();
        this.name = astralObject.getName();
        this.nbStatellites = astralObject.getSatellites().size();
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

    public long getNbStatellites() {
        return nbStatellites;
    }

    public void setNbStatellites(long nbStatellites) {
        this.nbStatellites = nbStatellites;
    }
}
