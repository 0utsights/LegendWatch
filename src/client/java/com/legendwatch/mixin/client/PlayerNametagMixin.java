package com.legendwatch.mixin.client;

import com.legendwatch.util.LegendSuffixUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
//? if >=1.21.2 {
import net.minecraft.client.render.entity.state.EntityRenderState;
//?} else {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;*/
//?}
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.21.2 {
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}

@Mixin(EntityRenderer.class)
public class PlayerNametagMixin {

    //? if >=1.21.2 {
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
    //?} else {
    /*@WrapOperation(
        method = "renderLabelIfPresent",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabel(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V")
    )
    private void appendLegendarySuffix(EntityRenderer<?> renderer, Entity entity, Text text,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int maxDistance,
            Operation<Void> original) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            text = LegendSuffixUtil.appendIfLegendary(text, player.getName().getString());
        }
        original.call(renderer, entity, text, matrices, vertexConsumers, light, maxDistance);
    }*/
    //?}
}
