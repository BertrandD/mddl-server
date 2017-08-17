package com.middlewar.dto;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PlayerDTO {

    private long id;
    private String name;
    private List<Long> bases;
    private Long currentBase;

    public PlayerDTO(Player player) {
        setId(player.getId());
        setName(player.getName());
        setBases(player.getBases().stream().map(Base::getId).collect(Collectors.toList()));
        if (player.getCurrentBase() != null)
            setCurrentBase(player.getCurrentBase().getId());
    }
}
