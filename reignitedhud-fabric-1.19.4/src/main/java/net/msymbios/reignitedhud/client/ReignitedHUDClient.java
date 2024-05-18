package net.msymbios.reignitedhud.client;

import net.fabricmc.api.ClientModInitializer;
import net.msymbios.reignitedhud.config.ReignitedHudConfig;
import net.msymbios.reignitedhud.gui.GuiWidget;

public class ReignitedHUDClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ReignitedHudConfig.register();
        GuiWidget.register();
    } // onInitializeClient ()

} // Class ReignitedHUDClient