package com.legendwatch.util;

import com.legendwatch.LegendwatchConfig;
import com.legendwatch.icons.LegendaryIcons;
import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class LegendSuffixUtil {

    public static Text appendIfLegendary(Text original, String username) {
        // Mod disabled; return vanilla nametag untouched.
        if (!LegendwatchConfig.modEnabled.get()) return original;

        List<LegendaryInfo> crafts = CraftTracker.getCrafts(username);
        if (crafts.isEmpty()) return original;

        MutableText result = Text.empty().append(original);

        for (LegendaryInfo info : crafts) {
            if (info.predicted && !LegendwatchConfig.predictedEnabled.get()) continue;

            result.append(Text.literal(" "));
            String scoreSuffix = getExperimentalScoreSuffix(info);

            if (LegendwatchConfig.iconsEnabled.get()) {
                result.append(LegendaryIcons.getDisplay(info.itemName));
                if (!scoreSuffix.isEmpty()) {
                    result.append(Text.literal(scoreSuffix).formatted(Formatting.GOLD));
                }
                if (info.predicted) {
                    result.append(Text.literal("?").formatted(Formatting.GRAY));
                }
            } else {
                String label = info.itemName + scoreSuffix;
                if (info.predicted) {
                    result.append(Text.literal(label + "?")
                            .formatted(Formatting.GRAY, Formatting.ITALIC));
                } else {
                    result.append(Text.literal(label).formatted(Formatting.GOLD));
                }
            }
        }

        return result;
    }

    private static String getExperimentalScoreSuffix(LegendaryInfo info) {
        if (!LegendwatchConfig.experimentalMidasTrackingEnabled.get()) return "";
        if (!CraftTracker.MIDAS_SWORD.equals(info.itemName)) return "";
        if (info.killCount <= 0) return "";
        return " " + info.killCount;
    }
}
