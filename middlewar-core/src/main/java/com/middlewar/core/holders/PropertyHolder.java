package com.middlewar.core.holders;

/**
 * @author LEBOC Philippe
 */
public class PropertyHolder {

    private String name;
    private String value;

    public PropertyHolder(String name, String value) {
        setName(name);
        setValue(value);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\t\t\tPropertyHolder(" + getName() + ", " + getValue() + ")\r\n";
    }
}
