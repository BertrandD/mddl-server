package com.middlewar.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
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
public class PlayerDto {

    private int id;
    private String name;
    private List<Integer> bases;
    private Integer currentBase;

    public PlayerDto(Player player) {
        this(
            player.getId(),
            player.getName(),
            player.getBases().stream().map(Base::getId).collect(toList()),
            player.getCurrentBase().getId()
        );
    }
}
