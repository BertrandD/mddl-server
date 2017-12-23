package com.middlewar.dto.buildings;

import com.middlewar.dto.BuildingDto;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ItemFactoryDTO extends BuildingDto {
    private HashMap<Integer, List<String>> itemsByLevel;

}
