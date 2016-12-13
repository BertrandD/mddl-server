package com.middlewar.core.model.shop;

/**
 * @author LEBOC Philippe
 */
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

    public String getItemId() {
        return itemId;
    }

    private void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public long getCount() {
        return count;
    }

    private void setCount(long count) {
        this.count = count;
    }

    public long getPrice() {
        return price;
    }

    private void setPrice(long price) {
        this.price = price;
    }

    public boolean isDisabled() {
        return disabled;
    }

    private void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString(){
        return "[ShopItem] " + itemId + " x" + count + " for " + price + "$";
    }
}
