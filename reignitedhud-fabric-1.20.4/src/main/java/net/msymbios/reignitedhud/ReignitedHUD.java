package net.msymbios.reignitedhud;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReignitedHUD implements ModInitializer {

	// -- Variables --
	public static final String MODID = "reignitedhud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	// -- Methods --

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	} // onInitialize ()

} // Class ReignitedHUDEvents