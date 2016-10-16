package com.gameserver.model.social;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.holders.PlayerHolder;
import com.serializer.FriendRequestSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "friend_request")
@JsonSerialize(using = FriendRequestSerializer.class)
public class FriendRequest {

    @Id
    private String id;
    private PlayerHolder requester;
    private PlayerHolder requested;
    private String message;
    private long requestDate;

    public FriendRequest(PlayerHolder requester, PlayerHolder requested, String message) {
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

    public PlayerHolder getRequester() {
        return requester;
    }

    public void setRequester(PlayerHolder requester) {
        this.requester = requester;
    }

    public PlayerHolder getRequested() {
        return requested;
    }

    public void setRequested(PlayerHolder requested) {
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
