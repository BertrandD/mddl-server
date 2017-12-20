package com.middlewar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AccountDTO {
    private int id;
    private String lang;
    private List<Integer> players;
    private int currentPlayer;
    private String token;
    private String username;
}
