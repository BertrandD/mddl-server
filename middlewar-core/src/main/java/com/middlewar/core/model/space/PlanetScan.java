package com.middlewar.core.model.space;

import com.middlewar.core.model.projections.BasePlanetScanProjection;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
    private long id;

    private long date;

    @ManyToOne
    private Planet planet;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Map<Integer, BasePlanetScanProjection> baseScanned;

    public PlanetScan(Planet planet) {
        this.date = TimeUtil.getCurrentTime();
        this.planet = planet;
        this.baseScanned = new HashMap<>();
    }
}
