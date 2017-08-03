package com.middlewar.cli;

import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bertrand
 */
@Slf4j
public class APICall {

    private static void handleErrror(Exception e) {
        if (e instanceof  RestClientResponseException) {
            log.debug("Error : " + ((RestClientResponseException)e).getRawStatusCode());
            log.debug(((RestClientResponseException)e).getResponseBodyAsString());
        } else {
            e.printStackTrace();
        }
    }

    private static  <T> T call(String url, Class<T> responseType, Map<String, String> uriParams) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (GameContext.getInstance().getAccount() != null) {
            headers.add("X-auth-token", GameContext.getInstance().getAccount().getToken());
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        T object;
        try {
            object = restTemplate.postForObject(CLIConfig.SERVER + url, entity, responseType, uriParams);
            return object;
        } catch (RestClientResponseException e) {
            handleErrror(e);
            return null;
        }
    }

    public static Account login(String username, String password) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("username", username);
        uriParams.put("password", password);

        return call("/login?username={username}&password={password}", Account.class, uriParams);
    }

    public static Account register(String username, String password) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("username", username);
        uriParams.put("password", password);

        return call("/register?username={username}&password={password}", Account.class, uriParams);
    }

    public static Player createPlayer(String name) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("name", name);

        return call("/player?name={name}", Player.class, uriParams);
    }
}
