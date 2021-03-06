package com.middlewar.core.model.social;

import com.middlewar.core.model.Player;
import com.middlewar.core.utils.TimeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Player requester;

    @ManyToOne
    private Player requested;

    private String message;

    private long requestDate;

    public FriendRequest(Player requester, Player requested, String message) {
        setRequester(requester);
        setRequested(requested);
        setMessage(message);
        setRequestDate(TimeUtil.getCurrentTime());
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof FriendRequest && ((FriendRequest) o).getId() == getId();
    }
}
