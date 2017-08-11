package com.middlewar.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Bertrand
 */
@Slf4j
class APIClient {

    private static void handleErrror(Exception e) {
        if (e instanceof  RestClientResponseException) {
            log.debug("Error : " + ((RestClientResponseException)e).getRawStatusCode());
            log.debug(((RestClientResponseException)e).getResponseBodyAsString());
        } else {
            e.printStackTrace();
        }
    }

    static  <T> T call(String url, Class<T> responseType, Map<String, String> uriParams) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (!ClientConfig.TOKEN.isEmpty()) {
            headers.add("X-auth-token", ClientConfig.TOKEN);
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        T object;
        try {
            object = restTemplate.postForObject(ClientConfig.SERVER + url, entity, responseType, uriParams);
            return object;
        } catch (RestClientResponseException e) {
            handleErrror(e);
            return null;
        }
    }
}
