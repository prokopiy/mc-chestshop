package com.github.prokopiy.mcchestshop;

public class Permissions {

//    public static final String LIST_BANNED_ITEMS = "placerestrict.commands.list";

    private static final String PLUGIN_ID = "mc-chestshop";
    //For specific items, Use as: mmcrestrict.bypass.[use/own].[ItemID/*] , ItemID can be found using /restrict whatsthis or /whatsthis with an item in hand
    public static final String BYPASS_LIMITED_BLOCK = PLUGIN_ID + ".bypass";

    public static final String WHATS_THIS   = PLUGIN_ID + ".commands.whatsthis";
    public static final String BLOCK_ADD    = PLUGIN_ID + ".commands.block.add";
    public static final String BLOCK_REMOVE = PLUGIN_ID + ".commands.block.remove";
    public static final String UPDATE_BLOCK = PLUGIN_ID + ".commands.block.update";

}

