package com.middlewar.dto.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InventoryDTO {
    List<ItemInstanceDTO> items;
}
