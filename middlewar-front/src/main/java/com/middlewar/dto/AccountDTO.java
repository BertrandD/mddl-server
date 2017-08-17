package com.middlewar.dto;

import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AccountDTO {
    private long id;
    private Lang lang;
    private List<Long> players;
    private long currentPlayer;
    private String token;
    private String username;

    public AccountDTO(Account account) {
        setId(account.getId());
        setLang(account.getLang());
        setPlayers(account.getPlayers().stream().map(Player::getId).collect(Collectors.toList()));
        setCurrentPlayer(account.getCurrentPlayer());
        setToken(account.getToken());
        setUsername(account.getUsername());
    }
}
