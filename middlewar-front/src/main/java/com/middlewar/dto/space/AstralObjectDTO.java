package com.middlewar.dto.space;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AstralObjectDTO {

    private long id;
    private String name;
    private double revolution;
    private double orbit;
    private double size;
    private List<AstralObjectDTO> satellites;
}
