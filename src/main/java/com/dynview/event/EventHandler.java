package com.dynview.event;

import com.dynview.Utils.TickTimeHandler;
import com.dynview.ViewDistHandler.ServerDynamicViewDistanceManager;
import com.dynview.command.EntryPoint;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onDedicatedServerTick(final TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            return;
        }
        TickTimeHandler.getInstance().onServerTick(event.getServer());
    }

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onWorldLoad(final LevelEvent.Load event)
    {
        ServerDynamicViewDistanceManager.getInstance().initViewDist(event.getLevel().getServer());
    }

    @SubscribeEvent
    public static void onCommandsRegister(final RegisterCommandsEvent event)
    {
        EntryPoint.register(event.getDispatcher());
    }
}
