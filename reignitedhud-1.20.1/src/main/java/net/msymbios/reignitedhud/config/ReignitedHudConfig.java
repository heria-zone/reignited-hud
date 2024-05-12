package net.msymbios.reignitedhud.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.msymbios.reignitedhud.ReignitedHud;

@Mod.EventBusSubscriber(modid = ReignitedHud.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ReignitedHudConfig {

    // -- Variables --
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ITEM_EQUIPMENT;
    public static final ForgeConfigSpec.BooleanValue ITEM_HAND;

    public static final ForgeConfigSpec.BooleanValue CLOCK_TIME;
    public static final ForgeConfigSpec.BooleanValue EFFECT;
    public static final ForgeConfigSpec.BooleanValue MOUNT;

    public static final ForgeConfigSpec.BooleanValue PLAYER_SKIN;
    public static final ForgeConfigSpec.BooleanValue PLAYER_USERNAME;
    public static final ForgeConfigSpec.BooleanValue PLAYER_HEALTH;
    public static final ForgeConfigSpec.BooleanValue PLAYER_AIR_SUPPLY;
    public static final ForgeConfigSpec.BooleanValue PLAYER_ARMOR;

    public static final ForgeConfigSpec.BooleanValue FOOD_LEVEL;
    public static final ForgeConfigSpec.BooleanValue FOOD_SATURATION;

    static {
        BUILDER.push("Reignited HUD");

        // DEFINE THE CONFIG

        PLAYER_SKIN = BUILDER.comment("Show/Hide Player Skins").define("player.skin", false);
        PLAYER_USERNAME = BUILDER.comment("Show/Hide Player Username").define("player.username", true);
        PLAYER_HEALTH = BUILDER.comment("Show/Hide Player Health").define("player.health", true);
        PLAYER_AIR_SUPPLY = BUILDER.comment("Show/Hide Player Air Supply").define("player.air", true);
        PLAYER_ARMOR = BUILDER.comment("Show/Hide Player Armor").define("player.armor", true);

        FOOD_LEVEL = BUILDER.comment("Show/Hide Food Level").define("food.value", true);
        FOOD_SATURATION = BUILDER.comment("Show/Hide Food Saturation").define("food.saturation", true);

        ITEM_EQUIPMENT = BUILDER.comment("Show/Hide Items Durability").define("item.equipment", true);
        ITEM_HAND = BUILDER.comment("Show/Hide Items Durability").define("item.hand", true);

        EFFECT = BUILDER.comment("Show/Hide Current Active Effects").define("misc.effect", true);
        MOUNT = BUILDER.comment("Show/Hide Mount Information").define("misc.mount", true);
        CLOCK_TIME = BUILDER.comment("Show/Hide Clock").define("misc.time", true);

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