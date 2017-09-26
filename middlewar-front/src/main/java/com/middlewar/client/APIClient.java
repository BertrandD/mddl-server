package com.middlewar.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    static  <T> T get(String url, Class<T> responseType, Map<String, String> uriParams) {
        return call(HttpMethod.GET, url, responseType, uriParams);
    }

    static  <T> T post(String url, Class<T> responseType, Map<String, String> uriParams) {
        return call(HttpMethod.POST, url, responseType, uriParams);
    }

    static  private <T> T call(HttpMethod httpMethod, String url, Class<T> responseType, Map<String, String> uriParams) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (!ClientConfig.TOKEN.isEmpty()) {
            headers.add("X-auth-token", ClientConfig.TOKEN);
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(ClientConfig.SERVER + url, httpMethod, entity, responseType, uriParams);
            log.debug(responseEntity.toString());
            return responseEntity.getBody();
        } catch (RestClientResponseException e) {
            handleErrror(e);
            return null;
        }
    }
}
