package com.middlewar.core.model.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Player;
import com.middlewar.core.serializer.PrivateMessageSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@NoArgsConstructor
@Entity
@JsonSerialize(using = PrivateMessageSerializer.class)
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
    public boolean equals(Object o){
        if(o instanceof PrivateMessage){
            final PrivateMessage pm = (PrivateMessage) o;
            if(pm.getId()== this.getId()) return true;
        }
        return false;
    }
}
