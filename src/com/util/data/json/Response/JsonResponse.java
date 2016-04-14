package com.util.data.json.Response;

/**
 * @author Bertrand
 */
public class JsonResponse {

    private JsonResponseType status;
    private Object payload;
    private Metadata metadata;

    public JsonResponse(JsonResponseType status){
        setStatus(status);
        setPayload(null);
        setMetadata(null);
    }

    public JsonResponse(JsonResponseType status, String message) {
        setStatus(status);
        setPayload(null);
        setMetadata(new Metadata(message));
    }

    public JsonResponse(JsonResponseType status, Object payload, String message) {
        setStatus(status);
        setPayload(payload);
        setMetadata(new Metadata(message));
    }

    public JsonResponseType getStatus() {
        return status;
    }

    public void setStatus(JsonResponseType status) {
        this.status = status;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    class Metadata{
        private String message;
        Metadata(String message){
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
