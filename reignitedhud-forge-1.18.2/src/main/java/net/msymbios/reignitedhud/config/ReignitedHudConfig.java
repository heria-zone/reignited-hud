package net.msymbios.reignitedhud.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.msymbios.reignitedhud.ReignitedHud;

public class ReignitedHudConfig {

    // -- Variables --
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue PLAYER_SKIN;
    public static final ForgeConfigSpec.BooleanValue PLAYER_USERNAME;
    public static final ForgeConfigSpec.BooleanValue PLAYER_HEALTH;
    public static final ForgeConfigSpec.BooleanValue PLAYER_AIR_SUPPLY;

    public static final ForgeConfigSpec.BooleanValue FOOD_LEVEL;
    public static final ForgeConfigSpec.BooleanValue FOOD_SATURATION;

    public static final ForgeConfigSpec.BooleanValue ARMOR_LEVEL;
    public static final ForgeConfigSpec.BooleanValue ARMOR_TOUGHNESS;

    public static final ForgeConfigSpec.BooleanValue ITEM_EQUIPMENT;
    public static final ForgeConfigSpec.BooleanValue ITEM_HAND;

    public static final ForgeConfigSpec.BooleanValue CLOCK_TIME;
    public static final ForgeConfigSpec.BooleanValue EFFECT;
    public static final ForgeConfigSpec.BooleanValue MOUNT;

    // Set up configurations for the Reignited HUD
    static {
        BUILDER.push("Reignited HUD");

        // Define player-related configurations
        PLAYER_SKIN = BUILDER.comment("Toggle Player Skins").define("player.skin", true);
        PLAYER_USERNAME = BUILDER.comment("Toggle Player Username").define("player.username", true);
        PLAYER_HEALTH = BUILDER.comment("Toggle Player Health").define("player.health", true);
        PLAYER_AIR_SUPPLY = BUILDER.comment("Toggle Player Air Supply").define("player.air", true);

        // Define armor-related configurations
        ARMOR_LEVEL = BUILDER.comment("Toggle Armor Level Display").define("armor.level", true);
        ARMOR_TOUGHNESS = BUILDER.comment("Toggle Armor Toughness Display").define("armor.toughness", true);

        // Define food-related configurations
        FOOD_LEVEL = BUILDER.comment("Toggle Food Level Display").define("food.value", true);
        FOOD_SATURATION = BUILDER.comment("Toggle Food Saturation Display").define("food.saturation", true);

        // Define item-related configurations
        ITEM_EQUIPMENT = BUILDER.comment("Toggle Items Durability Display").define("item.equipment", true);
        ITEM_HAND = BUILDER.comment("Toggle Items Durability Display").define("item.hand", true);

        // Define miscellaneous configurations
        EFFECT = BUILDER.comment("Toggle Active Effects Display").define("misc.effect", true);
        MOUNT = BUILDER.comment("Toggle Mount Information Display").define("misc.mount", true);
        CLOCK_TIME = BUILDER.comment("Toggle Clock Display").define("misc.time", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    // -- Methods --

    /**
     * Registers the configuration for the client.
     **/
    public static void register() {
        // Registering the client configuration
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SPEC, ReignitedHud.MODID + "-client.toml");
    } // register ()

} // Class ReignitedHudConfig