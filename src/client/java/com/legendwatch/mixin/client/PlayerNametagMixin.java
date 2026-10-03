package com.legendwatch.mixin.client;

import com.legendwatch.util.LegendSuffixUtil;
//? if >=26.1 {
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
//?} else {
/*import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;*/
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class PlayerNametagMixin {

    //? if >=26.1 {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void appendLegendarySuffix(Entity entity, EntityRenderState state,
                                        float tickDelta, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayer player)) return;
        if (state.nameTag == null) return;

        state.nameTag = LegendSuffixUtil.appendIfLegendary(
                state.nameTag,
                player.getName().getString()
        );
    }
    //?} else {
    /*@Inject(method = "updateRenderState", at = @At("TAIL"))
    private void appendLegendarySuffix(Entity entity, EntityRenderState state,
                                        float tickDelta, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayerEntity player)) return;
        if (state.displayName == null) return;

        state.displayName = LegendSuffixUtil.appendIfLegendary(
                state.displayName,
                player.getName().getString()
        );
    }*/
    //?}
}
