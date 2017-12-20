package com.middlewar.core.model.space;

import com.middlewar.core.enums.AstralStat;
import com.middlewar.core.utils.TimeUtil;
import com.middlewar.dto.space.AstralObjectDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

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
import java.util.stream.Collectors;

/**
 * @author bertrand Darbond
 */
@Data
@NoArgsConstructor
@Entity
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

    public AstralObjectDTO toDTO() {
        AstralObjectDTO dto = new AstralObjectDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setRevolution(this.getRevolution());
        dto.setOrbit(this.getOrbit());
        dto.setSize(this.getSize());
        dto.setSatellites(this.getSatellites().stream().map(AstralObject::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public double getAngle() {
        return ((2 * Math.PI) / (revolution)) * TimeUtil.getCurrentTime() / (6000)/* + theta0 */;
    }

    public String toString() {
        if (parent == null) {
            return name;
        }
        return parent.toString() + " - " + name;
    }
}
