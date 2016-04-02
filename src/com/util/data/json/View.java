package com.util.data.json;

/**
 * @author LEBOC Philippe
 */
public class View {

    // TODO: send only id from collections
    public interface Id{}

    // Common Attributes
    public interface Standard{}

    // Player
    public interface PlayerBases extends Standard{}

    // Bases
    public interface Base_Onwer extends Standard{}
    public interface Base_OwnerAndBuildings extends Base_Onwer{}

    // BuildingInstance
    public interface BuildingInstance_Base extends Standard{}

    // ItemInstance
    public interface ItemInstance_Base extends Standard{}
}
