package com.dynview.event;

import com.dynview.Utils.TickTimeHandler;
import com.dynview.ViewDistHandler.ServerDynamicViewDistanceManager;
import com.dynview.command.EntryPoint;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    @SubscribeEvent
    public static void onDedicatedServerTick(final ServerTickEvent.Post event)
    {
        TickTimeHandler.getInstance().onServerTick(event.getServer());
    }

    @SubscribeEvent
    public static void onServerStart(final ServerStartedEvent event)
    {
        ServerDynamicViewDistanceManager.getInstance().initViewDist(event.getServer());
    }

    @SubscribeEvent
    public static void onCommandsRegister(final RegisterCommandsEvent event)
    {
        EntryPoint.register(event.getDispatcher());
    }
}
