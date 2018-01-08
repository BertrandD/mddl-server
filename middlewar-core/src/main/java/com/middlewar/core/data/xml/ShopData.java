package com.middlewar.core.data.xml;

import com.middlewar.core.interfaces.IXmlReader;
import com.middlewar.core.model.shop.ShopCategory;
import com.middlewar.core.model.shop.ShopItem;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Slf4j
public class ShopData implements IXmlReader {

    private final HashMap<ShopCategory, List<ShopItem>> shop = new HashMap<>();

    protected ShopData() {
        load();
    }

    public static ShopData getInstance() {
        return SingletonHolder._instance;
    }

    @Override
    public synchronized void load() {
        shop.clear();
        parseFile(new File("classpath:/data/shop.xml"));
        log.info("Loaded " + shop.size() + " shop categories");
        long shopItemCount = 0;
        for (List<ShopItem> list : shop.values()) {
            shopItemCount += list.size();
        }
        log.info("Loaded " + shopItemCount + " items in shop");
    }

    @Override
    public void parseDocument(Document doc, File f) {
        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling()) {
            if ("list".equalsIgnoreCase(a.getNodeName())) {
                NamedNodeMap attrs = a.getAttributes();
                if (parseBoolean(attrs, "disabled", false)) continue;

                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling()) {
                    if ("category".equalsIgnoreCase(b.getNodeName())) {
                        attrs = b.getAttributes();
                        final ShopCategory shopCategory = parseEnum(attrs, ShopCategory.class, "name", null);
                        final List<ShopItem> items = new ArrayList<>();
                        for (Node c = b.getFirstChild(); c != null; c = c.getNextSibling()) {
                            if ("item".equalsIgnoreCase(c.getNodeName())) {
                                attrs = c.getAttributes();
                                final String itemId = parseString(attrs, "id", null);
                                final long count = parseLong(attrs, "count", 0L);
                                final long price = parseLong(attrs, "price", 0L);
                                final boolean disabled = parseBoolean(attrs, "disabled", false);

                                if (price == 0 || count == 0) continue;

                                final ShopItem si = new ShopItem(itemId, count, price, disabled);
                                items.add(si);
                            }
                        }
                        shop.put(shopCategory, items);
                    }
                }
            }
        }
    }

    private static class SingletonHolder {
        protected static final ShopData _instance = new ShopData();
    }
}
