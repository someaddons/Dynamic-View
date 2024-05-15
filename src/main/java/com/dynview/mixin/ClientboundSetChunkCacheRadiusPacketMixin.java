package com.dynview.mixin;

import com.dynview.DynView;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSetChunkCacheRadiusPacket.class)
public class ClientboundSetChunkCacheRadiusPacketMixin
{
    @Shadow
    @Final
    @Mutable
    private int radius;

    @Inject(method = "<init>(I)V", at = @At("RETURN"))
    private void onInit(final int i, final CallbackInfo ci)
    {
        radius = DynView.config.getCommonConfig().maxChunkViewDist;
    }
}
