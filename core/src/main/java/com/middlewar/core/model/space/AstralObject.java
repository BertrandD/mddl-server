package com.middlewar.core.model.space;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.AstralStat;
import com.middlewar.core.serializer.AstralObjectSerializer;
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
 * @author bertrand.
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

    @DBRef
    private List<AstralObject> satellites;

    @DBRef
    @JsonBackReference
    private AstralObject parent;

    public AstralObject(String name, AstralObject parent) {
        this.name = name;
        this.stats = new HashMap<>();
        this.satellites = new ArrayList<>();
        this.parent = parent;
        this.id = new ObjectId().toString();
    }

    public double getAngle() {
        return ((2 * Math.PI) / (revolution)) * System.currentTimeMillis() / (6000)/* + theta0 */;
    }
}
