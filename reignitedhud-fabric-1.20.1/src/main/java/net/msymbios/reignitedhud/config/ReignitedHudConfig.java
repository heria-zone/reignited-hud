package net.msymbios.reignitedhud.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.msymbios.reignitedhud.ReignitedHUD;

@Config(name = ReignitedHUD.MODID)
public class ReignitedHudConfig implements ConfigData {

    // -- Variables --
    @ConfigEntry.Gui.Excluded
    public static ReignitedHudConfig INSTANCE;

    @ConfigEntry.Category("player") @ConfigEntry.Gui.Tooltip()
    public static boolean PLAYER_SKIN = true;
    @ConfigEntry.Category("player") @ConfigEntry.Gui.Tooltip()
    public static boolean PLAYER_USERNAME = true;
    @ConfigEntry.Category("player") @ConfigEntry.Gui.Tooltip()
    public static boolean PLAYER_HEALTH = true;
    @ConfigEntry.Category("player") @ConfigEntry.Gui.Tooltip()
    public static boolean PLAYER_AIR_SUPPLY = true;

    @ConfigEntry.Category("food") @ConfigEntry.Gui.Tooltip()
    public static boolean FOOD_LEVEL = true;
    @ConfigEntry.Category("food") @ConfigEntry.Gui.Tooltip()
    public static boolean FOOD_SATURATION = true;

    @ConfigEntry.Category("armor") @ConfigEntry.Gui.Tooltip()
    public static boolean ARMOR_LEVEL = true;
    @ConfigEntry.Category("armor") @ConfigEntry.Gui.Tooltip()
    public static boolean ARMOR_TOUGHNESS = true;

    @ConfigEntry.Category("item") @ConfigEntry.Gui.Tooltip()
    public static boolean ITEM_EQUIPMENT = true;
    @ConfigEntry.Category("item") @ConfigEntry.Gui.Tooltip()
    public static boolean ITEM_HAND = true;

    @ConfigEntry.Gui.Tooltip()
    public static boolean CLOCK_TIME = true;
    @ConfigEntry.Gui.Tooltip()
    public static boolean EFFECT = true;
    @ConfigEntry.Gui.Tooltip()
    public static boolean MOUNT = true;

    // -- Methods --

    public static void register() {
        AutoConfig.register(ReignitedHudConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ReignitedHudConfig.class).getConfig();
    } // register ()

} // Class ReignitedHudConfig