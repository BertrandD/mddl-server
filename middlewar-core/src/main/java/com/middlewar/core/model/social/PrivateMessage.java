package com.middlewar.core.model.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PrivateMessage {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Player author;

    @ManyToOne
    private Player receiver;

    private long date;

    private String message;

    private boolean isRead;

    @JsonIgnore
    private long readDate;

    public PrivateMessage(Player author, Player receiver, String message) {
        setAuthor(author);
        setReceiver(receiver);
        setDate(TimeUtil.getCurrentTime());
        setMessage(message);
        setRead(false);
        setReadDate(0);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof PrivateMessage && ((PrivateMessage) o).getId() == getId();
    }
}
