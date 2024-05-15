package com.dynview.ViewDistHandler;

import com.dynview.DynView;
import net.minecraft.server.MinecraftServer;

public class ServerDynamicViewDistanceManager implements IDynamicViewDistanceManager
{
    private static final int                              UPDATE_LEEWAY = 3;
    private static       ServerDynamicViewDistanceManager instance;

    private boolean reduceViewDistance   = true;
    private boolean increaseViewDistance = true;

    private int             currentChunkViewDist   = 0;
    private int             currentChunkUpdateDist = 0;
    private MinecraftServer server;

    private ServerDynamicViewDistanceManager()
    {
    }

    public static IDynamicViewDistanceManager getInstance()
    {
        if (instance == null)
        {
            instance = new ServerDynamicViewDistanceManager();
        }
        return instance;
    }

    @Override
    public void initViewDist(final MinecraftServer server)
    {
        currentChunkViewDist = (DynView.config.getCommonConfig().minChunkViewDist + DynView.config.getCommonConfig().maxChunkViewDist) / 2;
        currentChunkUpdateDist = (DynView.config.getCommonConfig().minSimulationDist + DynView.config.getCommonConfig().maxSimulationDist) / 2;
        server.getPlayerList().setViewDistance(currentChunkViewDist);
        if (DynView.config.getCommonConfig().adjustSimulationDistance)
        {
            server.getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(currentChunkUpdateDist));
        }
        this.server = server;
    }

    @Override
    public void updateViewDistForMeanTick(final int meanTickTime)
    {
        if (server.getPlayerList().getPlayers().isEmpty())
        {
            return;
        }

        if (meanTickTime - UPDATE_LEEWAY > DynView.config.getCommonConfig().meanAvgTickTime)
        {
            increaseViewDistance = true;

            if (reduceViewDistance && currentChunkViewDist > DynView.config.getCommonConfig().minChunkViewDist)
            {
                reduceViewDistance = !DynView.config.getCommonConfig().adjustSimulationDistance;
                currentChunkViewDist--;
                if (DynView.config.getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing chunk view distance to: " + currentChunkViewDist);
                }
                server.getPlayerList().setViewDistance(currentChunkViewDist);
                return;
            }

            if (!reduceViewDistance && currentChunkUpdateDist > DynView.config.getCommonConfig().minSimulationDist)
            {
                reduceViewDistance = true;
                currentChunkUpdateDist--;
                if (DynView.config.getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing simulation distance to: " + currentChunkUpdateDist);
                }
                server.getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(currentChunkUpdateDist));
            }

            if (!DynView.config.getCommonConfig().adjustSimulationDistance)
            {
                reduceViewDistance = true;
            }
        }

        if (meanTickTime + UPDATE_LEEWAY < DynView.config.getCommonConfig().meanAvgTickTime)
        {
            reduceViewDistance = false;

            if (increaseViewDistance && currentChunkViewDist < DynView.config.getCommonConfig().maxChunkViewDist)
            {
                increaseViewDistance = !DynView.config.getCommonConfig().adjustSimulationDistance;
                currentChunkViewDist++;
                if (DynView.config.getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing chunk view distance to: " + currentChunkViewDist);
                }
                server.getPlayerList().setViewDistance(currentChunkViewDist);
                return;
            }

            if (!increaseViewDistance && currentChunkUpdateDist < DynView.config.getCommonConfig().maxSimulationDist)
            {
                increaseViewDistance = true;
                currentChunkUpdateDist++;
                if (DynView.config.getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing simulation distance to: " + currentChunkUpdateDist);
                }
                server.getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(currentChunkUpdateDist));
            }

            if (!DynView.config.getCommonConfig().adjustSimulationDistance)
            {
                increaseViewDistance = true;
            }
        }
    }
}
