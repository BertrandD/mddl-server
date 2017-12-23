package com.middlewar.dto.space;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.middlewar.core.model.space.AstralObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.stream.Collectors.toList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AstralObjectDto {

    private long id;
    private String name;
    private double revolution;
    private double orbit;
    private double size;
    private List<AstralObjectDto> satellites;

    public AstralObjectDto(final AstralObject object) {
        this(
            object.getId(),
            object.getName(),
            object.getRevolution(),
            object.getOrbit(),
            object.getSize(),
            object.getSatellites().stream().map(AstralObjectDto::new).collect(toList())
        );
    }
}
