package com.middlewar.core.model.social;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Player;
import com.middlewar.core.serializer.FriendRequestSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@NoArgsConstructor
@JsonSerialize(using = FriendRequestSerializer.class)
public class FriendRequest {

    @Id
    @GeneratedValue
    private String id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Player requester;
    @ManyToOne(cascade = CascadeType.ALL)
    private Player requested;
    private String message;
    private long requestDate;

    public FriendRequest(Player requester, Player requested, String message) {
        setRequester(requester);
        setRequested(requested);
        setMessage(message);
        setRequestDate(TimeUtil.getCurrentTime());
    }
}
