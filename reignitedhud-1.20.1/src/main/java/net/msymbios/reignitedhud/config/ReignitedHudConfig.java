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

    public static final ForgeConfigSpec.BooleanValue DURABILITY;
    public static final ForgeConfigSpec.BooleanValue TARGET;
    public static final ForgeConfigSpec.BooleanValue TIME;
    public static final ForgeConfigSpec.BooleanValue EFFECT;
    public static final ForgeConfigSpec.BooleanValue MOUNT;

    static {
        BUILDER.push("Reignited HUD");

        // DEFINE THE CONFIG
        DURABILITY = BUILDER.comment("Show/Hide Items Durability").define("durability", true);
        EFFECT = BUILDER.comment("Show/Hide Current Active Effects").define("effect", true);
        MOUNT = BUILDER.comment("Show/Hide Mount Information").define("mount", true);
        TARGET = BUILDER.comment("Show/Hide Entity Information").define("target", true);
        TIME = BUILDER.comment("Show/Hide Clock").define("time", true);

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