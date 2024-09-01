package com.dynview.ViewDistHandler;

import net.minecraft.server.MinecraftServer;

/**
 * Interface for dynamic view distance managers.
 */
public interface IDynamicViewDistanceManager
{
    void initViewDist(final MinecraftServer server);

    /**
     * Updates the current view distance based on server load.
     *
     * @param meanTick mean tick time
     */
    void updateViewDistForMeanTick(final int meanTick, final long tickBacklog);

    void setCurrentChunkViewDist(int currentChunkViewDist);

    void setCurrentChunkUpdateDist(int currentChunkUpdateDist);
}
