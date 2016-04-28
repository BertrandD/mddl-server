package com.gameserver.model.commons;

/**
 * @author LEBOC Philippe
 */
public final class SystemMessageId {

    // account
    public static final String USERNAME_NOT_FOUND = "username_not_found";
    public static final String USERNAME_ALREADY_EXIST = "username_already_exist";
    public static final String ACCOUNT_ALREADY_EXIST = "account_already_exist";

    // commons
    public static final String INCORRECT_CREDENTIALS = "incorrect_credentials";
    public static final String FORBIDDEN_NAME = "forbidden_name";
    public static final String CHOOSE_PLAYER = "choose_player";

    // player
    public static final String PLAYER_NOT_FOUND = "player_not_found";

    // base
    public static final String BASE_NOT_FOUND = "base_not_found";
    public static final String BASE_POSITION_ALREADY_TAKEN = "base_position_already_taken";

    // static building
    public static final String STATIC_BUILDING_DOESNT_EXIST = "static_building_doesnt_exist";

    // building
    public static final String BUILDING_CANNOT_CREATE = "building_cannot_create";
    public static final String BUILDING_NOT_FOUND = "building_not_found";
    public static final String BUILDING_ALREADY_EXIST = "building_already_exist";
    public static final String BUILDING_MAX_LEVEL_REACHED = "building_max_level_reached";

}
