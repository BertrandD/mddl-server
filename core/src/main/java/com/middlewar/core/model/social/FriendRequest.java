package com.middlewar.core.model.social;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.serializer.FriendRequestSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Data
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
        setRequestDate(TimeUtil.getCurrentTime());
    }
}
