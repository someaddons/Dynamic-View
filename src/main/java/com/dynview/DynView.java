package com.dynview;

import com.cupboard.config.CupboardConfig;
import com.dynview.config.CommonConfiguration;
import com.dynview.event.EventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MOD_ID)
public class DynView
{
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * The config instance.
     */
    public static final CupboardConfig<CommonConfiguration> config = new CupboardConfig<>("dynamicview", new CommonConfiguration());

    public DynView(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(EventHandler.class);
    }
}
