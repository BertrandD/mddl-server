package com.middlewar.core.model.space;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.AstralStat;
import com.middlewar.core.serializer.AstralObjectSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bertrand Darbond
 */
@Data
@JsonSerialize(using = AstralObjectSerializer.class)
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AstralObject {

    @Id
    @GeneratedValue
    private String id;

    private String name;

    private HashMap<AstralStat, Double> stats;

    // Nombre de minutes nécessaires pour faire 1 tour autour de parent
    private double revolution;

    private double orbit;

    private double size;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AstralObject> satellites;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
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

    public String toString() {
        if (parent == null) {
            return name;
        }
        return parent.toString() + " - " + name;
    }
}
