package net.msymbios.reignitedhud.common;

public class HudAPI {

    // -- Methods --

    /**
     * Get the current config for TES.
     * <p>Config is only available on the client side</p>
     */
    @Nullable
    public static TESConfig getConfig() {
        return TESConstants.CONFIG;
    }

} // Class HudAPI