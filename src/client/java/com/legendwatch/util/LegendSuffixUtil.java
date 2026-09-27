package com.legendwatch.util;

import com.legendwatch.LegendwatchConfig;
import com.legendwatch.icons.LegendaryIcons;
import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
//? if >=26.1 {
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
//?} else {
/*import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
*///?}

import java.util.List;

public class LegendSuffixUtil {

    //? if >=26.1 {
    public static Component appendIfLegendary(Component original, String username) {
    //?} else {
    /*public static Text appendIfLegendary(Text original, String username) {
    *///?}
        // Mod disabled; return vanilla nametag untouched.
        if (!LegendwatchConfig.modEnabled.get()) return original;

        List<LegendaryInfo> crafts = CraftTracker.getCrafts(username);
        if (crafts.isEmpty()) return original;

        //? if >=26.1 {
        MutableComponent result = Component.empty().append(original);
        //?} else {
        /*MutableText result = Text.empty().append(original);
        *///?}

        for (LegendaryInfo info : crafts) {
            if (info.predicted && !LegendwatchConfig.predictedEnabled.get()) continue;

            //? if >=26.1 {
            result.append(Component.literal(" "));
            //?} else {
            /*result.append(Text.literal(" "));
            *///?}
            String scoreSuffix = getExperimentalScoreSuffix(info);

            if (LegendwatchConfig.iconsEnabled.get()) {
                result.append(LegendaryIcons.getDisplay(info.itemName));
                if (!scoreSuffix.isEmpty()) {
                    //? if >=26.1 {
                    result.append(Component.literal(scoreSuffix).withStyle(ChatFormatting.GOLD));
                    //?} else {
                    /*result.append(Text.literal(scoreSuffix).formatted(Formatting.GOLD));
                    *///?}
                }
                if (info.predicted) {
                    //? if >=26.1 {
                    result.append(Component.literal("?").withStyle(ChatFormatting.GRAY));
                    //?} else {
                    /*result.append(Text.literal("?").formatted(Formatting.GRAY));
                    *///?}
                }
            } else {
                String label = info.itemName + scoreSuffix;
                if (info.predicted) {
                    //? if >=26.1 {
                    result.append(Component.literal(label + "?")
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    //?} else {
                    /*result.append(Text.literal(label + "?")
                            .formatted(Formatting.GRAY, Formatting.ITALIC));
                    *///?}
                } else {
                    //? if >=26.1 {
                    result.append(Component.literal(label).withStyle(ChatFormatting.GOLD));
                    //?} else {
                    /*result.append(Text.literal(label).formatted(Formatting.GOLD));
                    *///?}
                }
            }
        }

        return result;
    }

    private static String getExperimentalScoreSuffix(LegendaryInfo info) {
        if (!CraftTracker.MIDAS_SWORD.equals(info.itemName)) return "";
        if (info.killCount <= 0) return "";

        LegendwatchConfig.MidasDisplayMode displayMode = LegendwatchConfig.midasDisplayMode.get();
        return switch (displayMode) {
            case SHARPNESS_ESTIMATE -> " S" + estimateMidasSharpness(info.killCount);
            case KILL_COUNT -> " x" + info.killCount;
            case OFF -> "";
        };
    }

    private static int estimateMidasSharpness(int killCount) {
        if (killCount <= 0) return 0;
        if (killCount <= 2) return killCount;

        // Kills 1 and 2 are guaranteed sharpness. After that, estimate a level on odd kills.
        return 2 + ((killCount - 1) / 2);
    }
}
