package com.middlewar.core.model.space;

import com.middlewar.core.enums.AstralStat;
import com.middlewar.core.utils.TimeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bertrand Darbond
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AstralObject {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    private Map<AstralStat, Double> stats;

    // Nombre de minutes nécessaires pour faire 1 tour autour de parent
    private double revolution;

    private double orbit;

    private double size;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AstralObject> satellites;

    @ManyToOne
    private AstralObject parent;

    public AstralObject(String name, AstralObject parent) {
        this.name = name;
        this.stats = new HashMap<>();
        this.satellites = new ArrayList<>();
        this.parent = parent;
    }

    public double getAngle() {
        return ((2 * Math.PI) / (revolution)) * TimeUtil.getCurrentTime() / (6000)/* + theta0 */;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof AstralObject && ((AstralObject) o).getId() == getId();
    }
}
