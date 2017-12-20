package com.middlewar.dto;

import com.middlewar.dto.instances.BuildingInstanceDTO;
import lombok.Data;

@Data
public class BuildingTaskDTO {
    private long id;
    private BuildingInstanceDTO building;
    private long endsAt;
    private int level;
}
