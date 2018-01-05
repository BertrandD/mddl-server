package com.middlewar.core.model.tasks;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@Table(name = "buildingtasks")
@NoArgsConstructor
@AllArgsConstructor
public class BuildingTask {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @ManyToOne
    private Base base;

    @NotNull
    @OneToOne
    private BuildingInstance building;

    private long endsAt;

    private int level;

    public BuildingTask(Base base, BuildingInstance building, long endsAt, int level) {
        this(-1, base, building, endsAt, level);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof BuildingTask && ((BuildingTask) o).getId() == getId();
    }
}
