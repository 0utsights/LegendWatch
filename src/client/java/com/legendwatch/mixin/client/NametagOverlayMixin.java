package com.legendwatch.mixin.client;

import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class NametagOverlayMixin {

    @Unique
    private static final int OVERLAY_COLOR = 0xFFFFD700;

    @Inject(method = "renderLabelIfPresent", at = @At("TAIL"))
    private <S extends net.minecraft.client.render.entity.state.EntityRenderState> void onRenderLabel(S state, MatrixStack matrices,
                                                                                                      OrderedRenderCommandQueue queue, CameraRenderState cameraState,
                                                                                                      CallbackInfo ci) {

        if (!(state instanceof PlayerEntityRenderState playerState)) return;
        if (playerState.playerName == null) return;

        String username = playerState.playerName.getString()
                .replaceAll("§[0-9A-FK-ORa-fk-or]", "")
                .trim();

        if (username.isEmpty()) return;

        LegendaryInfo info = CraftTracker.getCraft(username);
        if (info == null) return;

        String overlayText = "✦ Crafted: " + info.itemName;

        // Submit our overlay label slightly above the vanilla nametag
        Vec3d labelPos = playerState.nameLabelPos;
        if (labelPos == null) return;

        Vec3d overlayPos = new Vec3d(labelPos.x, labelPos.y + 0.3, labelPos.z);

        queue.submitLabel(matrices, overlayPos, 0,
                Text.literal(overlayText),
                !playerState.sneaking,
                playerState.light,
                playerState.squaredDistanceToCamera,
                cameraState);
    }
}