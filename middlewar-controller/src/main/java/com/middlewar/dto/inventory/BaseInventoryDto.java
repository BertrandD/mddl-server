package com.middlewar.dto.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.middlewar.core.model.inventory.BaseInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseInventoryDto extends InventoryDto {

    long availableCapacity;

    public BaseInventoryDto(final BaseInventory inventory) {
        super(inventory);
        setAvailableCapacity(inventory.getAvailableCapacity());
    }
}
