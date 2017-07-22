package com.middlewar.core.model.space;

import com.middlewar.core.holders.AstralObjectHolder;
import com.middlewar.core.holders.BaseHolder;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bertrand.
 */
@Data
@Entity
@NoArgsConstructor
public class PlanetScan {

    @Id
    @GeneratedValue
    private String id;
    private long date;
    @ManyToOne
    private AstralObjectHolder planet;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Map<String, BaseHolder> baseScanned;

    public PlanetScan(Planet planet) {
        this.date = TimeUtil.getCurrentTime();
        this.planet = new AstralObjectHolder(planet);
        this.baseScanned = new HashMap<>();
    }
}
