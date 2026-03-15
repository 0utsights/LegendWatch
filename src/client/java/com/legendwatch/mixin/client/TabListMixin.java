package com.legendwatch.mixin.client;

import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class TabListMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void appendLegendarySuffix(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        // Get the raw username from the game profile
        String username = entry.getProfile()git add.getName();

        LegendaryInfo info = CraftTracker.getCraft(username);
        if (info == null) return;

        // Append the legendary item name in gold to whatever text vanilla returned
        Text modified = cir.getReturnValue().copy()
                .append(Text.literal(" " + info.itemName)
                        .formatted(Formatting.GOLD));

        cir.setReturnValue(modified);
    }
}
