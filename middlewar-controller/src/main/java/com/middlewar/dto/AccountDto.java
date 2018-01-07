package com.middlewar.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.middlewar.core.model.Account;
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
public class AccountDto {

    private int id;
    private String username;
    private String token;
    private int currentPlayer;
    private List<PlayerDto> players;

    public AccountDto(final Account account, final String token) {
        this(
            account.getId(),
            account.getUsername(),
            token,
            -1,
            account.getPlayers().stream().map(PlayerDto::new).collect(toList())
        );
    }
}
