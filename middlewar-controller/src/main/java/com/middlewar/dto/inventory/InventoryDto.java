package com.middlewar.dto.inventory;

import com.middlewar.core.model.inventory.Inventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private List<ItemInstanceDto> items;

    /**
     * Default constructor
     * @param inventory the inventory to map
     */
    public InventoryDto(final Inventory inventory) {
        this(inventory.getItems().stream().map(ItemInstanceDto::new).collect(toList()));
    }
}
