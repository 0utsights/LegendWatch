package com.legendwatch.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.legendwatch.util.LegendSuffixUtil;
//? if >=26.1 {
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
//?} else {
/*import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=26.1 {
@Mixin(PlayerTabOverlay.class)
//?} else {
/*@Mixin(PlayerListHud.class)
*///?}
public class TabListMixin {

    //? if >=26.1 {
    @ModifyReturnValue(method = "getNameForDisplay", at = @At("RETURN"))
    private Component appendLegendarySuffix(Component original, PlayerInfo entry) {
        String username = (entry.getTabListDisplayName() != null
                ? entry.getTabListDisplayName().getString()
                : original.getString())
                .replaceAll("§[0-9a-fk-or]", "")
                .replaceAll("[^\\x00-\\x7F]", "")
                .trim();

        return LegendSuffixUtil.appendIfLegendary(original, username);
    }
    //?} else {

    /*@ModifyReturnValue(method = "getPlayerName", at = @At("RETURN"))
    private Text appendLegendarySuffix(Text original, PlayerListEntry entry) {
        // getDisplayName() is the server-set name (includes rank prefix on Hoplite).
        // Strip color codes to get the raw username for our map lookup.
        String username = (entry.getDisplayName() != null
                ? entry.getDisplayName().getString()
                : original.getString())
                .replaceAll("§[0-9a-fk-or]", "")
                .replaceAll("[^\\x00-\\x7F]", "")
                .trim();

        return LegendSuffixUtil.appendIfLegendary(original, username);
    }

    *///?}
}
