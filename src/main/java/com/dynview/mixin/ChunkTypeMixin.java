package com.dynview.mixin;

import com.dynview.DynView;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Comparator;

@Mixin(TicketType.class)
public class ChunkTypeMixin
{
    @Shadow
    @Final
    @Mutable
    public static TicketType<ChunkPos> UNKNOWN =
      TicketType.create("unknown", Comparator.comparingLong(ChunkPos::toLong), DynView.getConfig().getCommonConfig().chunkunload ? 1201 : 1);
}
