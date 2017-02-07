package com.middlewar.core.model.tasks;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.serializer.BuildingTaskSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Data
@NoArgsConstructor
@Document(collection = "building_tasks")
@JsonSerialize(using = BuildingTaskSerializer.class)
public class BuildingTask {

    @Id
    private String id;

    @DBRef
    private Base base;

    @DBRef
    private BuildingInstance building;

    private long endsAt;
    private int level;

    public BuildingTask(Base base, BuildingInstance building, long endsAt, int level) {
        setId(new ObjectId().toString());
        setBase(base);
        setBuilding(building);
        setEndsAt(endsAt);
        setLevel(level);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BuildingTask) {
            final BuildingTask task = (BuildingTask)o;
            return (this.id.equals(task.id));
        }
        return false;
    }
}
