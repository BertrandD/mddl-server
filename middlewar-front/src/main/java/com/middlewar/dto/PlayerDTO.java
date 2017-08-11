package com.middlewar.dto;

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
    private List<BaseDTO> bases;
    private BaseDTO currentBase;

    public PlayerDTO(Player player) {
        setId(player.getId());
        setName(player.getName());
        setBases(player.getBases().stream().map(BaseDTO::new).collect(Collectors.toList()));
        if (player.getCurrentBase() != null)
            setCurrentBase(new BaseDTO(player.getCurrentBase()));
    }
}
