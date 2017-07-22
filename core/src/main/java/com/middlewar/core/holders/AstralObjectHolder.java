package com.middlewar.core.holders;

import com.middlewar.core.model.space.AstralObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author bertrand.
 */
@Data
public class AstralObjectHolder {
    @Id
    private String id;
    private String name;
    private long nbStatellites;
    
    public AstralObjectHolder(AstralObject astralObject) {
        this.id = astralObject.getId();
        this.name = astralObject.getName();
        this.nbStatellites = astralObject.getSatellites().size();
    }
}
