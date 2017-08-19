package com.middlewar.dto.instances;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BuildingInstanceDTO {

    private long id;
    private String buildingId;
    private int currentLevel;
    private long endsAt;
    private long startedAt;
    private List<String> modules;
}
