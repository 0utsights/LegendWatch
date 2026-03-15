package com.legendwatch.mixin.client;

import com.legendwatch.util.LegendSuffixUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class PlayerNametagMixin {

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void appendLegendarySuffix(Entity entity, EntityRenderState state,
                                        float tickDelta, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayerEntity player)) return;
        if (state.displayName == null) return;

        state.displayName = LegendSuffixUtil.appendIfLegendary(
                state.displayName,
                player.getName().getString()
        );
    }
}
