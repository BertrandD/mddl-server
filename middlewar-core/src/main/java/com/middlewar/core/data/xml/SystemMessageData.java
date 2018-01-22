package com.middlewar.core.data.xml;

import com.middlewar.core.enums.Lang;
import com.middlewar.core.interfaces.IXmlReader;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

import static com.middlewar.core.enums.Lang.FR;
import static java.lang.ClassLoader.getSystemResource;
import static java.nio.file.Paths.get;

/**
 * @author LEBOC Philippe
 */
@Slf4j
public class SystemMessageData implements IXmlReader {

    private final HashMap<String, String> _english = new HashMap<>();

    protected SystemMessageData() {
        load();
    }

    public static SystemMessageData getInstance() {
        return SingletonHolder._instance;
    }

    @Override
    public synchronized void load() {
        _english.clear();
        try
        {
            parseDirectory(get(getSystemResource("data/messages").toURI()).toFile(), false);
            log.info("Loaded " + _english.size() + " English System Messages.");
        } catch (URISyntaxException e) {
            log.error("Cannot parse System message data", e);
        }
    }

    @Override
    public void parseDocument(Document doc, File f) {
        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling()) {
            if ("list".equalsIgnoreCase(a.getNodeName())) {
                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling()) {
                    if ("messages".equalsIgnoreCase(b.getNodeName())) {
                        NamedNodeMap attrs = b.getAttributes();
                        final Lang lang = parseEnum(attrs, Lang.class, "lang");
                        if(lang == FR) continue;

                        for (Node c = b.getFirstChild(); c != null; c = c.getNextSibling()) {
                            if ("message".equalsIgnoreCase(c.getNodeName())) {
                                attrs = c.getAttributes();
                                final String id = parseString(attrs, "id");
                                final String text = parseString(attrs, "text", null);
                                _english.put(id, text);
                            }
                        }
                    }
                }
            }
        }
    }

    public String getMessage(String id) {
        String msg = _english.get(id);
        if (msg == null) {
            log.info("Missing  translation for key `" + id + "` !");
            return "%" + id + "%";
        }
        return msg;
    }

    private static class SingletonHolder {
        protected static final SystemMessageData _instance = new SystemMessageData();
    }
}
