package com.middlewar.core.dto.space;

import com.middlewar.core.model.space.AstralObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AstralObjectDTO {

    private long id;
    private String name;
    private double revolution;
    private double orbit;
    private double size;
    private List<AstralObjectDTO> satellites;

    public AstralObjectDTO(AstralObject astralObject) {
        setId(astralObject.getId());
        setName(astralObject.getName());
        setRevolution(astralObject.getRevolution());
        setOrbit(astralObject.getOrbit());
        setSize(astralObject.getSize());
        setSatellites(astralObject.getSatellites().stream().map(AstralObjectDTO::new).collect(Collectors.toList()));
    }
}
