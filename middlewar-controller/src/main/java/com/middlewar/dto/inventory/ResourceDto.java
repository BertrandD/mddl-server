package com.middlewar.dto.inventory;

import com.middlewar.core.model.inventory.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto {

    private long lastRefresh;
    private double count;
    private long availableCapacity;
    private double prodPerHour;
    private String templateId;
    private String maxVolume;

    public ResourceDto(Resource resource) {
        this(
            resource.getLastRefresh(),
            resource.getCount(),
            0L,
            0.0,
            resource.getItem().getTemplateId(),
            null
        );
    }
}
