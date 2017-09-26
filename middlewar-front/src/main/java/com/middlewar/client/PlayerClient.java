package com.middlewar.client;

import com.middlewar.dto.PlayerDTO;

import java.util.HashMap;
import java.util.Map;

public class PlayerClient extends APIClient {
    public static PlayerDTO createPlayer(String name) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("name", name);

        return post(Route.PLAYER_CREATE + "?name={name}", PlayerDTO.class, uriParams);
    }

    public static PlayerDTO getPlayer(long currentPlayer) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("id", Long.toString(currentPlayer));

        return get(Route.PLAYER_ONE, PlayerDTO.class, uriParams);
    }
}
