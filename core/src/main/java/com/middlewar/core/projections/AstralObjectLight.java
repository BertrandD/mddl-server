package com.middlewar.core.projections;

import com.middlewar.core.model.space.AstralObject;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bertrand.
 * TODO: rename and move to holder
 */
@Data
@NoArgsConstructor
public class AstralObjectLight {
    private String id;
    private String name;
    private long nbStatellites;
    
    public AstralObjectLight(AstralObject astralObject) {
        this.id = astralObject.getId();
        this.name = astralObject.getName();
        this.nbStatellites = astralObject.getSatellites().size();
    }
}
