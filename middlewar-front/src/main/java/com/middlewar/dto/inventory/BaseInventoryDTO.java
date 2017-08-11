package com.middlewar.dto.inventory;

import com.middlewar.core.model.inventory.BaseInventory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseInventoryDTO {

    long availableCapacity;

    public BaseInventoryDTO(BaseInventory baseInventory) {
        setAvailableCapacity(baseInventory.getAvailableCapacity());
    }
}
