package com.middlewar.core.utils;

/**
 * @author LEBOC Philippe
 */
public final class SystemMessageId {

    // account
    public static final String USERNAME_NOT_FOUND = "username_not_found";
    public static final String USERNAME_ALREADY_EXIST = "username_already_exist";
    public static final String ACCOUNT_ALREADY_EXIST = "account_already_exist";

    // FriendRequest
    public static final String FRIEND_REQUEST_DOESNT_EXIST = "friendrequest_doesnt_exist";
    public static final String FAILED_TO_SEND_FRIEND_REQUEST = "friendrequest_faile_to_send_friend_request";
    public static final String YOU_CANNOT_REQUEST_YOURSELF = "friendrequest_you_cannot_request_yourself";

    // common
    public static final String INVALID_PARAMETERS = "INVALID PARAMETERS"; // Used to check Request parameters
    public static final String INCORRECT_CREDENTIALS = "incorrect_credentials";
    public static final String FORBIDDEN_NAME = "forbidden_name";
    public static final String CHOOSE_PLAYER = "choose_player";

    // player
    public static final String PLAYER_NOT_FOUND = "player_not_found";
    public static final String PLAYER_NOT_OWNED = "player_not_owned";
    public static final String MAXIMUM_PLAYER_CREATION_REACHED = "maximum_player_creation_reached";
    public static final String PLAYER_CREATION_FAILED = "player_creation_failed";
    public static final String PLAYER_HAS_NO_BASE = "player_has_no_base";

    // base
    public static final String BASE_NOT_FOUND = "base_not_found";
    public static final String BASE_CANNOT_CREATE = "base_cannot_create";
    public static final String BASE_NOT_OWNED = "base_not_owned";
    public static final String BASE_POSITION_ALREADY_TAKEN = "base_position_already_taken";

    // static building
    public static final String STATIC_BUILDING_DOESNT_EXIST = "static_building_doesnt_exist";

    // building
    public static final String BUILDING_CANNOT_CREATE = "building_cannot_create";
    public static final String BUILDING_NOT_FOUND = "building_not_found";
    public static final String BUILDING_TEMPLATE_NOT_FOUND = "building_template_not_found";
    public static final String BUILDING_ALREADY_EXIST = "building_already_exist";
    public static final String BUILDING_MAX_LEVEL_REACHED = "building_max_level_reached";

    // item
    public static final String ITEM_NOT_FOUND = "item_not_found";
    public static final String ITEM_NOT_FOUND_IN_INVENTORY = "item_not_found_in_inventory";
    public static final String ITEM_CANNOT_CREATE = "item_cannot_create";
    public static final String ITEM_NOT_UNLOCKED = "item_not_unlocked";
    public static final String BAD_ITEM = "bad_item";

    // inventory
    public static final String INVENTORY_NOT_FOUND = "inventory_not_found";

    // requirement
    public static final String YOU_DONT_MEET_BUILDING_REQUIREMENT = "you_dont_meet_building_requirement";
    public static final String YOU_DONT_MEET_ITEM_REQUIREMENT = "you_dont_meet_item_requirement";
    public static final String YOU_DONT_MEET_RESOURCE_REQUIREMENT = "you_dont_meet_resource_requirement";

    // spy report
    public static final String CANNOT_CREATE_SPY_REPORT = "cannot_create_spy_report";

    // internal
    public static final String INTERNAL_ERROR = "internal_error";

    public static final String MODULE_NOT_IN_INVENTORY = "module_not_in_inventory";
    public static final String MAXIMUM_MODULE_REACHED = "maximum_module_reached";
    public static final String MODULE_NOT_ALLOWED_HERE = "module_not_allowed_here";
    public static final String NOT_ENOUGH_MODULES = "not_enough_modules";

    //ship
    public static final String SHIP_CREATION_FAILED = "ship_creation_failed";

    // recipe
    public static final String RECIPE_NOT_FOUND = "recipe_not_found";
    public static final String RECIPE_CREATION_FAILED = "recipe_creation_failed";
    public static final String NOT_ENOUGH_SLOTS = "not_enough_slots";

}
