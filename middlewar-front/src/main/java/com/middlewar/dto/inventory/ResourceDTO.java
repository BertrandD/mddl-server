package com.middlewar.dto.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceDTO {
    private long lastRefresh;
    private double count;
    private long availableCapacity;
    private double prodPerHour;
}
