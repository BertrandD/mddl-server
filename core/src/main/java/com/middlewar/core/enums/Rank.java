package com.middlewar.core.enums;

/**
 * @author LEBOC Philippe
 * @since 19/03/2016
 */
public enum Rank {
    NONE("Aucun rang", "Rank description"),
    A("A", "Rank description"),
    B("B", "Rank description"),
    C("C", "Rank description"),
    D("D", "Rank description"),
    E("E", "Rank description"),
    F("F", "Rank description");

    private String _name;
    private String _description;

    Rank(String name, String description)
    {
        setName(name);
        setDescription(description);
    }

    public String getName() {
        return _name;
    }

    private void setName(String _name) {
        this._name = _name;
    }

    public String getDescription() {
        return _description;
    }

    private void setDescription(String _description) {
        this._description = _description;
    }
}
