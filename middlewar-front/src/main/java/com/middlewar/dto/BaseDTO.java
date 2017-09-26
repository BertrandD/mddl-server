package com.middlewar.dto;

import com.middlewar.dto.instances.BuildingInstanceDTO;
import com.middlewar.dto.inventory.BaseInventoryDTO;
import com.middlewar.dto.inventory.ResourceDTO;
import com.middlewar.dto.space.AstralObjectDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BaseDTO {

    private long id;
    private String name;
    private List<BuildingInstanceDTO> buildings;
    private BaseInventoryDTO baseInventory;
    private List<ResourceDTO> resources;
    private AstralObjectDTO planet;
}
