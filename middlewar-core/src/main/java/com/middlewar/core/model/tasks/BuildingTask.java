package com.middlewar.core.model.tasks;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
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

    public BuildingTaskDTO toDTO() {
        BuildingTaskDTO dto = new BuildingTaskDTO();
        dto.setId(getId());
        dto.setBuilding(building.toDTO());
        dto.setEndsAt(getEndsAt());
        dto.setLevel(getLevel());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BuildingTask) {
            final BuildingTask task = (BuildingTask) o;
            return (this.id == task.id);
        }
        return false;
    }


    @Override
    public int compareTo(BuildingTask o) {
        if (equals(o)) return 0;
        return this.endsAt < o.getEndsAt() ? -1 : 1;
    }
}
