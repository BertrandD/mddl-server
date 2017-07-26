package com.middlewar.core.model.shop;

import lombok.Data;

/**
 * @author LEBOC Philippe
 */
@Data
public class ShopItem {
    private String itemId;
    private long count;
    private long price;
    private boolean disabled;

    public ShopItem(String itemId, long count, long price, boolean disabled) {
        setItemId(itemId);
        setCount(count);
        setPrice(price);
        setDisabled(disabled);
    }

    @Override
    public String toString(){
        return "[ShopItem] " + itemId + " x" + count + " for " + price + "$";
    }
}
