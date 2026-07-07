package net.msymbios.reignitedhud;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.msymbios.reignitedhud.config.ReignitedHudConfig;
import net.msymbios.reignitedhud.gui.GuiWidget;
import org.slf4j.Logger;

@Mod(ReignitedHud.MODID)
public class ReignitedHud {

    // -- Variables --
    public static final String MODID = "reignitedhud";

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ReignitedHud() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();

        // some pre init code
        ReignitedHudConfig.register();

        event.addListener(this::common);
        event.addListener(this::client);

        MinecraftForge.EVENT_BUS.register(new GuiWidget());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }  // Constructor ReignitedHud ()

    private void common(final FMLCommonSetupEvent event) {
        // some pre init code
    } // common ()

    private void client(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //MinecraftForge.EVENT_BUS.register(new GuiWidget());
    } // client ()

} // Class ReignitedHud