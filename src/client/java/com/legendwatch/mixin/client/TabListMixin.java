package com.legendwatch.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.legendwatch.util.LegendSuffixUtil;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerListHud.class)
public class TabListMixin {

    @ModifyReturnValue(method = "getPlayerName", at = @At("RETURN"))
    private Text appendLegendarySuffix(Text original, PlayerListEntry entry) {
        // getDisplayName() is the server-set name (includes rank prefix on Hoplite).
        // Strip color codes to get the raw username for our map lookup.
        String username = (entry.getDisplayName() != null
                ? entry.getDisplayName().getString()
                : original.getString())
                .replaceAll("§[0-9a-fk-or]", "")
                .trim();

        return LegendSuffixUtil.appendIfLegendary(original, username);
    }
}
