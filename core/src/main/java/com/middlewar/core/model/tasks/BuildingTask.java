package com.middlewar.core.model.tasks;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.serializer.BuildingTaskSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@NoArgsConstructor
@Entity
@JsonSerialize(using = BuildingTaskSerializer.class)
public class BuildingTask {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Base base;

    @OneToOne
    private BuildingInstance building;

    private long endsAt;
    private int level;

    public BuildingTask(Base base, BuildingInstance building, long endsAt, int level) {
        setBase(base);
        setBuilding(building);
        setEndsAt(endsAt);
        setLevel(level);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BuildingTask) {
            final BuildingTask task = (BuildingTask)o;
            return (this.id == task.id);
        }
        return false;
    }
}
