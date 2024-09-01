package com.dynview.ViewDistHandler;

import com.dynview.DynView;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Random;

public class ServerDynamicViewDistanceManager implements IDynamicViewDistanceManager
{
    private static final int                              UPDATE_LEEWAY = 3;
    private static       ServerDynamicViewDistanceManager instance;
    private static       Random                           rand          = new Random();

    private int currentChunkViewDist   = 0;
    private int currentChunkUpdateDist = 0;

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
        server.getPlayerList().setViewDistance(currentChunkUpdateDist);
        if (DynView.config.getCommonConfig().adjustSimulationDistance)
        {
            ServerLifecycleHooks.getCurrentServer().getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(currentChunkUpdateDist));
        }
    }

    @Override
    public void updateViewDistForMeanTick(final int meanTickTime, final long tickBacklog)
    {
        final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server.getPlayerList().getPlayers().isEmpty())
        {
            return;
        }

        if ((meanTickTime - UPDATE_LEEWAY > DynView.config.getCommonConfig().meanAvgTickTime)
        || (tickBacklog > DynView.config.getCommonConfig().maxTickBacklog))
        {
            if (currentChunkUpdateDist > DynView.config.getCommonConfig().minSimulationDist && rand.nextInt(10) != 0)
            {
                currentChunkUpdateDist--;
                if (DynView.config.getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tickTime: " + meanTickTime + "ms (Tick Backlog: "+tickBacklog+") decreasing simulation distance to: " + currentChunkUpdateDist);
                }
                server.getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(currentChunkUpdateDist));
                return;
            }

            if (currentChunkViewDist > DynView.config.getCommonConfig().minChunkViewDist)
            {
                currentChunkViewDist--;
                if (DynView.config.getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tickTime:" + meanTickTime + "ms (Backlog:"+tickBacklog+") decreasing chunk view distance to: " + currentChunkViewDist);
                }
                server.getPlayerList().setViewDistance(currentChunkViewDist);
            }
        }

        if ((meanTickTime + UPDATE_LEEWAY < DynView.config.getCommonConfig().meanAvgTickTime)
        && (tickBacklog < DynView.config.getCommonConfig().maxTickBacklog))
        {
            if (currentChunkUpdateDist < DynView.config.getCommonConfig().maxSimulationDist && rand.nextInt(10) != 0)
            {
                currentChunkUpdateDist++;
                if (DynView.config.getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing simulation distance to: " + currentChunkUpdateDist);
                }
                server.getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(currentChunkUpdateDist));
                return;
            }

            if (currentChunkViewDist < DynView.config.getCommonConfig().maxChunkViewDist)
            {
                currentChunkViewDist++;
                if (DynView.config.getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing chunk view distance to: " + currentChunkViewDist);
                }
                server.getPlayerList().setViewDistance(currentChunkViewDist);
            }
        }
    }

    @Override
    public void setCurrentChunkViewDist(final int currentChunkViewDist)
    {
        this.currentChunkViewDist = Mth.clamp(currentChunkViewDist, 0, 200);
        ServerLifecycleHooks.getCurrentServer().getPlayerList().setViewDistance(this.currentChunkViewDist);
    }

    @Override
    public void setCurrentChunkUpdateDist(final int currentChunkUpdateDist)
    {
        this.currentChunkUpdateDist = Mth.clamp(currentChunkUpdateDist, 0, 200);
        ServerLifecycleHooks.getCurrentServer().getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(this.currentChunkUpdateDist));
    }
}
