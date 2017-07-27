package com.middlewar.core.holders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
public class PropertiesHolder {

    private HashMap<Integer, List<PropertyListHolder>> _propertiesByLevel;

    public PropertiesHolder() {
        setPropertiesByLevel(new HashMap<>());
    }

    public HashMap<Integer, List<PropertyListHolder>> getPropertiesByLevel() {
        return _propertiesByLevel;
    }

    public void setPropertiesByLevel(HashMap<Integer, List<PropertyListHolder>> _propertiesByLevel) {
        this._propertiesByLevel = _propertiesByLevel;
    }

    /**
     * add new level Group
     *
     * @param level
     */
    public List<PropertyListHolder> addLevelGroup(int level) {
        if (!getPropertiesByLevel().containsKey(level)) {
            final List<PropertyListHolder> list = new ArrayList<>();
            getPropertiesByLevel().put(level, list);
            return list;
        }
        return getPropertiesByLevel().get(level);
    }

    @Override
    public String toString() {
        String tostring = "\r\nPropertiesHolder()\r\n";
        for (Map.Entry<Integer, List<PropertyListHolder>> entry : getPropertiesByLevel().entrySet()) {
            tostring += "Level " + entry.getKey() + "\r\n";
            tostring += entry.getValue().toString();
        }
        return tostring;
    }
}
