package com.middlewar.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.dto.instances.BuildingInstanceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingTaskDto {

    private long id;
    private BuildingInstanceDto building;
    private long endsAt;
    private int level;

    /**
     * Default constructor
     * @param task the task to map
     */
    public BuildingTaskDto(final BuildingTask task) {
        this(
            task.getId(),
            new BuildingInstanceDto(task.getBuilding()),
            task.getEndsAt(),
            task.getLevel()
        );
    }
}
