package com.middlewar.core.model.space;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.AstralStat;
import com.middlewar.core.serializer.AstralObjectSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bertrand Darbond
 */
@Data
@JsonSerialize(using = AstralObjectSerializer.class)
@Document(collection = "universe")
public abstract class AstralObject {

    @Id
    private String id;

    @Indexed
    private String name;

    private HashMap<AstralStat, Double> stats;

    // Nombre de minutes nécessaires pour faire 1 tour autour de parent
    private double revolution;

    private double orbit;

    private double size;

    @DBRef(lazy = true)
    private List<AstralObject> satellites;

    @DBRef
    @JsonBackReference
    private AstralObject parent;

    public AstralObject(String name, AstralObject parent) {
        this.id = new ObjectId().toString();
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
