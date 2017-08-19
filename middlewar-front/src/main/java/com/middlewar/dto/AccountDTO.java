package com.middlewar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AccountDTO {
    private long id;
    private String lang;
    private List<Long> players;
    private long currentPlayer;
    private String token;
    private String username;
}
