package com.middlewar.core.dto.inventory;

import com.middlewar.core.model.inventory.Resource;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceDTO {
    private long lastRefresh;
    private double count;
    private long availableCapacity;
    private double prodPerHour;

    public ResourceDTO(Resource resource) {
        setLastRefresh(resource.getLastRefresh());
        setCount(resource.getCount());
        setAvailableCapacity(resource.getAvailableCapacity());
        setProdPerHour(resource.getProdPerHour());
    }
}
