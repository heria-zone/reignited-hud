package net.msymbios.reignitedhud.config;

import net.minecraft.util.ResourceLocation;
import net.msymbios.reignitedhud.ReignitedHud;

public class ReignitedHudID {

    // -- Variables --
    public static final ResourceLocation TEX_HUD_BASE = getId("textures/gui/hud_base.png");
    public static final ResourceLocation TEX_HUD_BAR = getId("textures/gui/hud_bar.png");
    public static final ResourceLocation TEX_HUD_ICON = getId("textures/gui/hud_icon.png");
    public static final ResourceLocation TEX_HUD_EFFECT = getId("textures/gui/hud_effects.png");

    // -- Methods --

    public static ResourceLocation getId(final String path) {
        return new ResourceLocation(ReignitedHud.MODID, path);
    } // getId ()

    public static ResourceLocation getId(final String namespace, final String path) {
        if (namespace == null || namespace.isEmpty())
            return getId(path);
        return new ResourceLocation(namespace, path);
    } // getId ()

} // Class ReignitedHudID