package com.middlewar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlayerDTO {

    private long id;
    private String name;
    private List<Long> bases;
    private Long currentBase;
}
