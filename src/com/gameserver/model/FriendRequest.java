package com.gameserver.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "friend_request")
public class FriendRequest {

    @Id
    private String id;
    private Player requester;
    private Player requested;
    private String message;
    private long requestDate;

    public FriendRequest(Player requester, Player requested, String message) {
        setId(new ObjectId().toString());
        setRequester(requester);
        setRequested(requested);
        setMessage(message);
        setRequestDate(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player getRequester() {
        return requester;
    }

    public void setRequester(Player requester) {
        this.requester = requester;
    }

    public Player getRequested() {
        return requested;
    }

    public void setRequested(Player requested) {
        this.requested = requested;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(long requestDate) {
        this.requestDate = requestDate;
    }
}
