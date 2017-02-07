package com.middlewar.core.model.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.serializer.PrivateMessageSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Data
@Document(collection = "private_messages")
@JsonSerialize(using = PrivateMessageSerializer.class)
public class PrivateMessage {

    @Id
    private String id;
    private PlayerHolder author;
    private PlayerHolder receiver;
    private long date;
    private String message;
    private boolean isRead;

    @JsonIgnore
    private long readDate;

    public PrivateMessage(PlayerHolder author, PlayerHolder receiver, String message) {
        setId(new ObjectId().toString());
        setAuthor(author);
        setReceiver(receiver);
        setDate(System.currentTimeMillis());
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
