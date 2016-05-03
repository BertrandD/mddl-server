package com.util.data.json;

/**
 * @author LEBOC Philippe
 */
public class View {

    public interface Id{}

    // Common Attributes
    public interface Standard{}

    public interface stdWithRequirement extends Standard{}

    // TODO: remove
    public interface player_account extends Standard{}
    public interface buildingInstance_base extends Standard{}
    public interface buildingInstance_full extends Standard{}
    public interface itemInstance_inventory extends Standard{}
}
