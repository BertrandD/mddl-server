package com.middlewar.client;

public class Route {

    // Default

    public static final String LOGIN    = "/login";
    public static final String REGISTER = "/register";

    // Static

    public static final String STATIC_BUILDING_ALL        = "/building_static";
    public static final String STATIC_BUILDING_ONE        = "/building_static/{id}";
    public static final String STATIC_ITEM_ALL            = "/item_static";
    public static final String STATIC_ITEM_COMMON         = "/item_static/common";
    public static final String STATIC_ITEM_COMMON_ONE     = "/item_static/common/{id}";
    public static final String STATIC_ITEM_CARGO          = "/item_static/cargo";
    public static final String STATIC_ITEM_CARGO_ONE      = "/item_static/cargo/{id}";
    public static final String STATIC_ITEM_ENGINE         = "/item_static/engine";
    public static final String STATIC_ITEM_ENGINE_ONE     = "/item_static/engine/{id}";
    public static final String STATIC_ITEM_MODULE         = "/item_static/module";
    public static final String STATIC_ITEM_MODULE_ONE     = "/item_static/module/{id}";
    public static final String STATIC_ITEM_STRUCTURE      = "/item_static/structure";
    public static final String STATIC_ITEM_STRUCTURE_ONE  = "/item_static/structure/{id}";
    public static final String STATIC_ITEM_WEAPON         = "/item_static/weapon";
    public static final String STATIC_ITEM_WEAPON_ONE     = "/item_static/weapon/{id}";

    // Player

    public static final String PLAYER_ALL       = "/players";
    public static final String PLAYER_ONE       = "/me/player/{id}";
    public static final String PLAYER_CREATE    = "/me/player";
    public static final String PLAYER_ALL_OWNED = "/me/player";

    // Friends

    public static final String REQUEST_ALL    = "/friend/request";
    public static final String REQUEST_CREATE = "/friend/request";
    public static final String REQUEST_ACCEPT = "/friend/request/{requestId}/accept";
    public static final String REQUEST_REFUSE = "/friend/request/{requestId}/refuse";

    // Private Messages

    public static final String PM_ALL  = "/pm";
    public static final String PM_SEND = "/pm";
    public static final String PM_ONE  = "/pm/{id}";

    // Base

    public static final String BASE_ALL       = "/me/base";
    public static final String BASE_CREATE    = "/me/base";
    public static final String BASE_ONE       = "/me/base/{id}";
    public static final String BASE_BUILDABLE = "/me/base/{id}/buildables";
    public static final String BASE_SPY       = "/me/base/{id}/spy/{target}";

    // Building Instance

    public static final String BUILDING_CREATE        = "/me/base/{baseId}/building";
    public static final String BUILDING_ONE           = "/me/base/{baseId}/building/{id}";
    public static final String BUILDING_UPGRADE       = "/me/base/{baseId}/building/{id}/upgrade";
    public static final String BUILDING_ATTACH_MODULE = "/me/base/{baseId}/building/{id}/attach/module/{module}";

    // Item Factory

    public static final String ITEM_FACTORY_CREATE_MODULE    = "/me/base/{baseId}/factory/{factoryId}/create/module/{id}";
    public static final String ITEM_FACTORY_CREATE_STRUCTURE = "/me/base/{baseId}/factory/{factoryId}/create/structure/{id}";

    // Ship

    public static final String SHIP_CREATE = "/me/ship";

    // Report

    public static final String REPORT_ALL = "/me/reports";

    // Space

    public static final String SPACE_SYSTEM_ONE  = "/space/system/{id}";
    public static final String SPACE_SYSTEM_MY   = "/me/space/system";
    public static final String SPACE_SYSTEM_SCAN = "/me/space/system/{id}/scan";
}
