package com.gameserver.data.xml.impl;

import com.config.Config;
import com.gameserver.enums.Lang;
import com.gameserver.model.commons.SystemMessage;
import com.util.data.xml.IXmlReader;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public class SystemMessageData implements IXmlReader {

    private final Logger LOGGER = Logger.getLogger(getClass().getSimpleName());
    private final HashMap<Integer, SystemMessage> _messages = new HashMap<>();

    protected SystemMessageData(){
        load();
    }

    @Override
    public void load() {
        _messages.clear();
        parseDatapackDirectory(Config.DATA_ROOT_DIRECTORY + "messages", false);
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _messages.size() + " System Messages.");
    }

    @Override
    public void parseDocument(Document doc) {
        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling()) {
            if ("list".equalsIgnoreCase(a.getNodeName())) {
                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling()) {
                    if ("messages".equalsIgnoreCase(b.getNodeName())) {
                        NamedNodeMap attrs = b.getAttributes();
                        final int id = parseInteger(attrs, "id");
                        final SystemMessage message = new SystemMessage(id);
                        for(Node c = b.getFirstChild(); c != null; c = c.getNextSibling()){
                            if("message".equalsIgnoreCase(c.getNodeName())){
                                attrs = c.getAttributes();
                                message.addMessage(parseEnum(attrs, Lang.class, "lang"), parseString(attrs, "text", null));
                            }
                        }
                        _messages.put(id, message);
                    }
                }
            }
        }
    }

    public static SystemMessageData getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final SystemMessageData _instance = new SystemMessageData();
    }
}
