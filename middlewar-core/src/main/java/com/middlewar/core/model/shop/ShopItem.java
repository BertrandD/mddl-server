package com.middlewar.core.model.shop;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
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
}
