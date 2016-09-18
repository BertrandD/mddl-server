package com.gameserver.model.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameserver.holders.PlayerHolder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "private_messages")
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
        setIsRead(false);
        setReadDate(0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlayerHolder getAuthor() {
        return author;
    }

    public void setAuthor(PlayerHolder author) {
        this.author = author;
    }

    public PlayerHolder getReceiver() {
        return receiver;
    }

    public void setReceiver(PlayerHolder receiver) {
        this.receiver = receiver;
    }

    public long getDate() {
        return date;
    }

    private void setDate(long date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    @JsonIgnore
    public long getReadDate() {
        return readDate;
    }

    public void setReadDate(long readDate) {
        this.readDate = readDate;
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
