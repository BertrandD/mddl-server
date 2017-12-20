package com.middlewar.dto.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseInventoryDTO extends InventoryDTO {
    long availableCapacity;
}
