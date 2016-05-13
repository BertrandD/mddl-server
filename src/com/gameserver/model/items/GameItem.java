package com.gameserver.model.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.ItemType;
import com.gameserver.enums.Lang;
import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public abstract class GameItem {

    private String itemId;

    @JsonIgnore
    private String nameId;

    private ItemType type;

    @JsonIgnore
    private String descriptionId;

    private long weight;

    private long volume;

    @JsonIgnore
    private Lang lang;

    public GameItem(StatsSet set){
        setItemId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", ItemType.class, ItemType.NONE));
        setDescriptionId(set.getString("descriptionId"));
        setWeight(set.getLong("weight"));
        setVolume(set.getLong("volume"));
        setLang(Lang.EN);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        final String name = SystemMessageData.getInstance().getMessage(getLang(), getNameId());
        if(name == null) return "Unamed item ["+getItemId()+"]";
        return name;
    }

    @JsonIgnore
    public String getNameId() {
        return nameId;
    }

    public void setNameId(String name) {
        this.nameId = name;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getDescription(){
        final String descr = SystemMessageData.getInstance().getMessage(getLang(), getDescriptionId());
        if(descr == null) return "No description for item ["+getItemId()+"]";
        return descr;
    }

    @JsonIgnore
    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @JsonIgnore
    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }
}
