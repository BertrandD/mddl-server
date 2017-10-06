package com.middlewar.dto;

import com.middlewar.dto.commons.RequirementDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class BuildingDTO {
    private String id;
    private String name;
    private String description;
    private String type;
    private Map<Integer, RequirementDTO> requirements;
    private int maxLevel;
    private int maxBuild;
    private long[] buildTimes;
    private long[] useEnergy;
}
