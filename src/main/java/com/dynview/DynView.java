package com.dynview;

import com.cupboard.config.CupboardConfig;
import com.dynview.config.CommonConfiguration;
import com.dynview.event.EventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
public class DynView implements ModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID  = "dynview";

    /**
     * The config instance.
     */
    public static final CupboardConfig<CommonConfiguration> config = new CupboardConfig<CommonConfiguration>("dynamicview", new CommonConfiguration());

    @Override
    public void onInitialize()
    {
        ServerLifecycleEvents.SERVER_STARTED.register(EventHandler::onServerStarted);
        ServerTickEvents.END_SERVER_TICK.register(EventHandler::onDedicatedServerTick);
    }
}
