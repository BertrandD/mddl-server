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
@Entity
@NoArgsConstructor
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
        return o != null && o instanceof BuildingTask && ((BuildingTask) o).getId() == getId();
    }
}
