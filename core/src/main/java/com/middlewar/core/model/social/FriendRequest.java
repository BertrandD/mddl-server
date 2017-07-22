package com.middlewar.core.model.social;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.serializer.FriendRequestSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = FriendRequestSerializer.class)
public class FriendRequest {

    @Id
    @GeneratedValue
    private String id;
    @ManyToOne
    private PlayerHolder requester;
    @ManyToOne
    private PlayerHolder requested;
    private String message;
    private long requestDate;

    public FriendRequest(PlayerHolder requester, PlayerHolder requested, String message) {
        setRequester(requester);
        setRequested(requested);
        setMessage(message);
        setRequestDate(TimeUtil.getCurrentTime());
    }
}
