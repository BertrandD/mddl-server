package com.middlewar.core.model.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.serializer.PrivateMessageSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = PrivateMessageSerializer.class)
public class PrivateMessage {

    @Id
    @GeneratedValue
    private String id;
    private PlayerHolder author;
    private PlayerHolder receiver;
    private long date;
    private String message;
    private boolean isRead;

    @JsonIgnore
    private long readDate;

    public PrivateMessage(PlayerHolder author, PlayerHolder receiver, String message) {
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
            if(pm.getId().equalsIgnoreCase(this.getId())) return true;
        }
        return false;
    }
}
