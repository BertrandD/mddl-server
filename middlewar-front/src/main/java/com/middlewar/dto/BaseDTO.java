package com.middlewar.dto;

import com.middlewar.dto.instances.BuildingInstanceDTO;
import com.middlewar.dto.inventory.BaseInventoryDTO;
import com.middlewar.dto.inventory.ResourceDTO;
import com.middlewar.dto.space.AstralObjectDTO;
import com.middlewar.dto.stats.ObjectStatDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BaseDTO {

    private int id;
    private String name;
    private List<BuildingInstanceDTO> buildings;
    private BaseInventoryDTO inventory;
    private List<ResourceDTO> resources;
    private AstralObjectDTO planet;
    private List<BuildingTaskDTO> queue;
    private ObjectStatDTO baseStat;
}
