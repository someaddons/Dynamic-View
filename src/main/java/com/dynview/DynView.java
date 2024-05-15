package com.dynview;

import com.cupboard.config.CupboardConfig;
import com.dynview.config.CommonConfiguration;
import com.dynview.event.EventHandler;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
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

    public DynView()
    {
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(EventHandler.class);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
          () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
    }
}
