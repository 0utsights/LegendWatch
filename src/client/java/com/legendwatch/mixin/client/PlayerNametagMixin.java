package com.legendwatch.mixin.client;

import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class PlayerNametagMixin {

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void appendLegendarySuffix(Entity entity, EntityRenderState state,
                                        float tickDelta, CallbackInfo ci) {
        // Only process other players, not the local player
        if (!(entity instanceof AbstractClientPlayerEntity player)) return;

        // Don't touch displayName if vanilla decided not to show it (sneaking, out of range, etc.)
        if (state.displayName == null) return;

        String username = player.getName().getString();
        LegendaryInfo info = CraftTracker.getCraft(username);
        if (info == null) return;

        // Append the legendary item name in gold directly to the nametag text
        state.displayName = state.displayName.copy()
                .append(Text.literal(" " + info.itemName)
                        .formatted(Formatting.GOLD));
    }
}
