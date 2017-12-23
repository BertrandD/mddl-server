package com.middlewar.dto.commons;

import com.middlewar.dto.holder.BuildingHolderDTO;
import com.middlewar.dto.holder.ItemHolderDTO;
import lombok.Data;

import java.util.List;

@Data
public class RequirementDTO {
    private int level;
    private List<ItemHolderDTO> items;
    private List<BuildingHolderDTO> buildings;
}
