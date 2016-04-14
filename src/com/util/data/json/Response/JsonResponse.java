package com.util.data.json.Response;

/**
 * @author Bertrand
 */
public class JsonResponse {

    private JsonResponseType status;
    private String message;

    public JsonResponse(JsonResponseType status){
        setStatus(status);
        setMessage(null);
    }

    public JsonResponse(JsonResponseType status, String message) {
        setStatus(status);
        setMessage(message);
    }

    public JsonResponseType getStatus() {
        return status;
    }

    public void setStatus(JsonResponseType status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
