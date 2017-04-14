package com.middlewar.core.holders;

import com.middlewar.core.model.space.AstralObject;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bertrand.
 */
@Data
@NoArgsConstructor
public class AstralObjectHolder {
    private String id;
    private String name;
    private long nbStatellites;
    
    public AstralObjectHolder(AstralObject astralObject) {
        this.id = astralObject.getId();
        this.name = astralObject.getName();
        this.nbStatellites = astralObject.getSatellites().size();
    }
}
