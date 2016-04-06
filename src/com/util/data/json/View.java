package com.util.data.json;

/**
 * @author LEBOC Philippe
 */
public class View {

    public interface Id{}

    // Common Attributes
    public interface Standard{}

    // Player
    public interface Player_Bases extends Standard{}
    public interface Player_Inventory extends Standard{}

    // Bases
    public interface Base_Onwer extends Standard{}
    public interface Base_Buildings extends Standard{}

    // BuildingInstance
    public interface BuildingInstance_Base extends Standard{}
    public interface BuildingInstance_Inventory extends Standard{}

    // ItemInstance
    public interface ItemInstance_Base extends Standard{}
}
