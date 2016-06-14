package com.gameserver.holders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class PropertyListHolder
{
    private String name;
    private List<PropertyHolder> properties;

    public PropertyListHolder(String name){
        setName(name);
        setProperties(new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PropertyHolder> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyHolder> properties) {
        this.properties = properties;
    }

    public void addProperty(PropertyHolder holder){
        getProperties().add(holder);
    }

    @Override
    public String toString() {
        String tostring = "\r\n\tPropertyListHolder(" + getName() + ")\r\n";
        for (PropertyHolder propertyHolder : getProperties()) {
            tostring += propertyHolder.toString();
        }
        return tostring;
    }
}