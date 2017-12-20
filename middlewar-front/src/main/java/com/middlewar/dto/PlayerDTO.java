package com.middlewar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlayerDTO {

    private int id;
    private String name;
    private List<Integer> bases;
    private Integer currentBase;
}
