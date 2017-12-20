package com.middlewar.dto.inventory;

import lombok.Data;

@Data
public class ItemInstanceDTO {
    private long id;
    private String templateId;
    private double count;
    private String type;
}
