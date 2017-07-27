package com.middlewar.core.enums;

/**
 * @author LEBOC Philippe
 */
public enum Lang {
    EN("English"),
    FR("Français");

    private String name;

    Lang(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
