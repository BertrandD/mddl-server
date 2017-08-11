package com.middlewar.dto;

import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AccountDTO {
    private long id;
    private Lang lang;
    private List<PlayerDTO> players;
    private long currentPlayer;
    private String token;
    private String username;

    public AccountDTO(Account account) {
        setId(account.getId());
        setLang(account.getLang());
        setPlayers(account.getPlayers().stream().map(PlayerDTO::new).collect(Collectors.toList()));
        setCurrentPlayer(account.getCurrentPlayer());
        setToken(account.getToken());
        setUsername(account.getUsername());
    }
}
