package com.middlewar.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.middlewar.core.model.Base;
import com.middlewar.dto.instances.BuildingInstanceDto;
import com.middlewar.dto.inventory.BaseInventoryDto;
import com.middlewar.dto.inventory.ResourceDto;
import com.middlewar.dto.space.AstralObjectDto;
import com.middlewar.dto.stats.ObjectStatDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.stream.Collectors.toList;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDto {

    private int id;
    private String name;
    private List<BuildingInstanceDto> buildings;
    private BaseInventoryDto inventory;
    private List<ResourceDto> resources;
    private AstralObjectDto planet;
    private List<BuildingTaskDto> queue;
    private ObjectStatDto baseStat;

    /**
     * Default constructor
     * @param base The vase to be mapped
     */
    public BaseDto(final Base base) {
        this(
            base.getId(),
            base.getName(),
            base.getBuildings().stream().map(BuildingInstanceDto::new).collect(toList()),
            new BaseInventoryDto(base.getBaseInventory()),
            base.getResources().stream().map(ResourceDto::new).collect(toList()),
            new AstralObjectDto(base.getPlanet()),
            base.getBuildingTasks().stream().map(BuildingTaskDto::new).collect(toList()),
            new ObjectStatDto(base.getBaseStat())
        );
    }
}
