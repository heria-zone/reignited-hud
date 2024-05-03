package net.msymbios.reignitedhud.config;

import net.minecraft.util.ResourceLocation;
import net.msymbios.reignitedhud.ReignitedHud;

public class ReignitedHudID {

    // -- Variables --

    public static final String MODNAME = "Reignited HUD";
    public static final String MODID = "reignitedhud";
    public static final String MODVERSION = "1.0.0";
    public static final String COMMONPROXY = "net.msymbios.reignitedhud.proxy.ProxyCommon";
    public static final String CLIENTPROXY = "net.msymbios.reignitedhud.proxy.ProxyClient";
    public static final String GUIFACTORY = "net.msymbios.reignitedhud.config.ConfigGuiFactory";

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