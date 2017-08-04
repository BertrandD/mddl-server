package com.middlewar.core.dto;

import com.middlewar.core.dto.instances.BuildingInstanceDTO;
import com.middlewar.core.dto.inventory.BaseInventoryDTO;
import com.middlewar.core.dto.inventory.ResourceDTO;
import com.middlewar.core.dto.space.AstralObjectDTO;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.stats.ObjectStat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BaseDTO {

    private long id;
    private String name;
    private ObjectStat baseStat;
    private List<BuildingInstanceDTO> buildings;
    private BaseInventoryDTO baseInventory;
    private List<ResourceDTO> resources;
    private AstralObjectDTO planet;

    public BaseDTO(Base base) {
        setId(base.getId());
        setName(base.getName());
        setBaseStat(base.getBaseStat());
        setBuildings(base.getBuildings().stream().map(BuildingInstanceDTO::new).collect(Collectors.toList()));
        setBaseInventory(new BaseInventoryDTO(base.getBaseInventory()));
        setResources(base.getResources().stream().map(ResourceDTO::new).collect(Collectors.toList()));
        setPlanet(new AstralObjectDTO(base.getPlanet()));
    }
}
