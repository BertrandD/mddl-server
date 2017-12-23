package com.middlewar.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.middlewar.core.enums.BuildingCategory;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.dto.commons.RequirementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingDto {

    private String id;
    private String name;
    private String description;
    private BuildingCategory type;
    private Map<Integer, RequirementDTO> requirements;
    private int maxLevel;
    private int maxBuild;
    private long[] buildTimes;
    private long[] useEnergy;

    /**
     * Default constructor
     * @param building the building to map
     */
    public BuildingDto(final Building building) {
        this(
            building.getId(),
            building.getName(),
            building.getDescription(),
            building.getType(),
            null, // TODO
            building.getMaxLevel(),
            building.getMaxBuild(),
            building.getBuildTimes(),
            building.getUseEnergy()
        );
    }
}
