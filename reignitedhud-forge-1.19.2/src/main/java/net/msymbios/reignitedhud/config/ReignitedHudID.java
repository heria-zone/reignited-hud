package net.msymbios.reignitedhud.config;

import net.minecraft.resources.ResourceLocation;
import net.msymbios.reignitedhud.ReignitedHud;

public class ReignitedHudID {

    // -- Variables --
    public static final ResourceLocation TEX_HUD_BASE = getId("textures/gui/hud_base.png");
    public static final ResourceLocation TEX_HUD_BAR = getId("textures/gui/hud_bar.png");
    public static final ResourceLocation TEX_HUD_ICON = getId("textures/gui/hud_icon.png");
    public static final ResourceLocation TEX_HUD_EFFECT = getId("textures/gui/hud_effects.png");

    // -- Methods --

    /**
     * Get the ResourceLocation based on the provided path.
     *
     * @param path The path to the resource.
     * @return The ResourceLocation object.
     */
    public static ResourceLocation getId(final String path) {
        return new ResourceLocation(ReignitedHud.MODID, path);
    } // getId ()

    /**
     * Get the ResourceLocation based on the provided namespace and path.
     * If the namespace is null or empty, it falls back to the default namespace (MODID).
     *
     * @param namespace The namespace for the resource.
     * @param path The path to the resource.
     * @return The ResourceLocation object.
     */
    public static ResourceLocation getId(final String namespace, final String path) {
        if (namespace == null || namespace.isEmpty()) return getId(path);
        return new ResourceLocation(namespace, path);
    } // getId ()

} // Class ReignitedHudID