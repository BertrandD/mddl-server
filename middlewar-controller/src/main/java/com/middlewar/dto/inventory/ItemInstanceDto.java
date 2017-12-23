package com.middlewar.dto.inventory;

import com.middlewar.core.enums.ItemType;
import com.middlewar.core.model.instances.ItemInstance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemInstanceDto {

    private long id;
    private String templateId;
    private double count;
    private ItemType type;

    /**
     * Default Constructor
     * @param instance the ItemInstance to map
     */
    public ItemInstanceDto(final ItemInstance instance) {
        this(instance.getId(), instance.getTemplateId(), instance.getCount(), instance.getType());
    }
}
