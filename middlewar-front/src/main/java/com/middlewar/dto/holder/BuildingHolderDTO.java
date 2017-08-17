package com.middlewar.dto.holder;

import com.middlewar.core.holders.BuildingHolder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BuildingHolderDTO {

    private String templateId;
    private int level;

    public BuildingHolderDTO(BuildingHolder buildingHolder) {
        setTemplateId(buildingHolder.getTemplateId());
        setLevel(buildingHolder.getLevel());
    }

}
