package com.util.data.json;

/**
 * @author Bertrand
 */
public class JsonErrorResponse {
    private String status;
    private String message;

    public JsonErrorResponse(String message) {
        this.status = "error";
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
