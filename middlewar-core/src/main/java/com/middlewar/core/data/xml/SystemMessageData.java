package com.middlewar.core.data.xml;

import com.middlewar.core.config.Config;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.interfaces.IXmlReader;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public class SystemMessageData implements IXmlReader {

    private final Logger LOGGER = Logger.getLogger(getClass().getSimpleName());
    private final HashMap<String, String> _english = new HashMap<>();
    private final HashMap<String, String> _french = new HashMap<>();

    protected SystemMessageData() {
        load();
    }

    public static SystemMessageData getInstance() {
        return SingletonHolder._instance;
    }

    @Override
    public synchronized void load() {
        _english.clear();
        _french.clear();
        parseDirectory(new File(Config.DATA_ROOT_DIRECTORY + "messages"), false);
        LOGGER.info(" Loaded " + _english.size() + " English System Messages.");
        LOGGER.info(" Loaded " + _french.size() + " French System Messages.");
    }

    @Override
    public void parseDocument(Document doc, File f) {
        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling()) {
            if ("list".equalsIgnoreCase(a.getNodeName())) {
                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling()) {
                    if ("messages".equalsIgnoreCase(b.getNodeName())) {
                        NamedNodeMap attrs = b.getAttributes();
                        final Lang lang = parseEnum(attrs, Lang.class, "lang");
                        final HashMap<String, String> current = getMessages(lang);
                        for (Node c = b.getFirstChild(); c != null; c = c.getNextSibling()) {
                            if ("message".equalsIgnoreCase(c.getNodeName())) {
                                attrs = c.getAttributes();
                                final String id = parseString(attrs, "id");
                                final String text = parseString(attrs, "text", null);
                                current.put(id, text);
                            }
                        }
                    }
                }
            }
        }
    }

    public String getMessage(Lang lang, String id) {
        String msg = getMessages(lang).get(id);
        if (msg == null) {
            //Slack.sendWarning("Missing " + lang.getName() + " translation for key `" + id + "` !");
            LOGGER.info("Missing " + lang.getName() + " translation for key `" + id + "` !");
            return "%" + id + "%";
        }
        return msg;
    }

    public HashMap<String, String> getMessages(Lang lang) {
        switch (lang) {
            case EN:
                return _english;
            case FR:
                return _french;
            default:
                return _english;
        }
    }

    private static class SingletonHolder {
        protected static final SystemMessageData _instance = new SystemMessageData();
    }
}
