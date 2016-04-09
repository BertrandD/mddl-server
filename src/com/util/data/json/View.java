package com.util.data.json;

/**
 * @author LEBOC Philippe
 */
public class View {

    public interface Id{}

    // Common Attributes
    public interface Standard{}

    // Player
    public interface player_bases extends Standard{}
    public interface player_inventory extends Standard{}

    // Bases
    public interface base_onwer extends Standard{}
    public interface base_buildings extends Standard{}

    // BuildingInstance
    public interface buildingInstance_base extends Standard{}
    public interface buildingInstance_inventory extends Standard{}
    public interface buildingInstance_full extends Standard{}

    // ItemInstance
    public interface itemInstance_inventory extends Standard{}
}
