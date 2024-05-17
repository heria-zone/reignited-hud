package net.msymbios.reignitedhud;

import net.fabricmc.api.ModInitializer;

import net.msymbios.reignitedhud.config.ReignitedHudConfig;
import net.msymbios.reignitedhud.gui.GuiWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReignitedHUD implements ModInitializer {

	// -- Variables --
	public static final String MODID = "reignitedhud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	// -- Methods --

	@Override
	public void onInitialize() {
		//ReignitedHudConfig.register();
		//GuiWidget.register();
	} // onInitialize ()

} // Class ReignitedHUDEvents