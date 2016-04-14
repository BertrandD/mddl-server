package com.gameserver.model.commons;

import com.gameserver.enums.Lang;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public class SystemMessage {

    private int id;
    private HashMap<Lang, String> messages;

    public SystemMessage(int id){
        setId(id);
        setMessages(new HashMap<>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Lang, String> getMessages() {
        return messages;
    }

    private void setMessages(HashMap<Lang, String> messages) {
        this.messages = messages;
    }

    public void addMessage(Lang lang, String message){
        getMessages().put(lang, message);
    }

    public String getMessage(Lang lang){
        return getMessage(lang);
    }
}
