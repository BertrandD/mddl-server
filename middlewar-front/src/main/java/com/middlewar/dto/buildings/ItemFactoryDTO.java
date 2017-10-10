package com.middlewar.dto.buildings;

import com.middlewar.dto.BuildingDTO;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ItemFactoryDTO extends BuildingDTO {
    private HashMap<Integer, List<String>> itemsByLevel;

}
