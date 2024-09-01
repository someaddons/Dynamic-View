package com.dynview.Utils;

import com.dynview.DynView;
import com.dynview.ViewDistHandler.ServerDynamicViewDistanceManager;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;

public class TickTimeHandler
{
    private int meanTickSum   = 50;
    private int meanTickCount = 1;
    private int tickTimer     = 0;
    private int agroScalebackLimiter = 0;

    public static int serverTickTimerInterval = 100;

    private static TickTimeHandler instance = new TickTimeHandler();

    public static TickTimeHandler getInstance()
    {
        return instance;
    }

    private TickTimeHandler()
    {
        // Intentionally empty
    }

    /**
     * Averages values each 20 ticks
     *
     * @param server
     */
    public void onServerTick(final MinecraftServer server)
    {
        long tickBacklog = (Util.getMillis() - server.getNextTickTime()) / 50;
//        LOGGER.warn("Tick backlog:"+tickBacklog);
        if ((DynView.config.getCommonConfig().aggressiveScalebackEnabled) &&
            (tickBacklog > agroScalebackLimiter) &&
            (tickBacklog > DynView.config.getCommonConfig().maxTickBacklog))
        {
            if (DynView.config.getCommonConfig().logMessages)
            {
                agroScalebackLimiter = Math.toIntExact(tickBacklog + 10); // Pause for 10 ticks, unless delay goes way up.
                DynView.LOGGER.info("Agressive scaleback. (Tick Backlog: "+tickBacklog+")");
            }
            ServerDynamicViewDistanceManager.getInstance().updateViewDistForMeanTick(serverMeanTickTime, tickBacklog);
        }
        if (agroScalebackLimiter > 0)
        {
            agroScalebackLimiter--;
        }
//        LOGGER.warn("MeanTickSum:"+meanTickSum+" meanTickCount:"+meanTickCount+" tickTimer:"+tickTimer);
        tickTimer++;

        if (tickTimer % 20 == 0)
        {
            meanTickSum += average(server.tickTimes) * 1.0E-6D;
            meanTickCount++;

            if (tickTimer >= serverTickTimerInterval)
            {
                serverMeanTickTime = meanTickSum / meanTickCount;
                tickTimer = 0;
                meanTickCount = 0;
                meanTickSum = 0;

                ServerDynamicViewDistanceManager.getInstance().updateViewDistForMeanTick(serverMeanTickTime, tickBacklog);
            }
        }
    }

    /**
     * Averages the arrays values.
     *
     * @param values
     * @return
     */
    private static long average(long[] values)
    {
        if (values == null || values.length == 0)
        {
            return 0L;
        }

        long sum = 0L;
        for (long v : values)
        {
            sum += v;
        }
        return sum / values.length;
    }

    private int serverMeanTickTime = 50;

    /**
     * Returns the mean tick time
     *
     * @return avg tick time
     */
    public int getMeanTickTime()
    {
        return serverMeanTickTime;
    }
}
