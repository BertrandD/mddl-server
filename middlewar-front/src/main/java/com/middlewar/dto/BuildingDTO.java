package com.middlewar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BuildingDTO {
    private String id;
    private String name;
    private String description;
    private String type;
    private int maxLevel;
    private int maxBuild;
    private long[] buildTimes;
    private long[] useEnergy;
}
