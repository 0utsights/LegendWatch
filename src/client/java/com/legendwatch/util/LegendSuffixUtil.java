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
        // Mod disabled — return vanilla nametag untouched
        if (!LegendwatchConfig.modEnabled.get()) return original;

        List<LegendaryInfo> crafts = CraftTracker.getCrafts(username);
        if (crafts.isEmpty()) return original;

        MutableText result = Text.empty().append(original);

        for (LegendaryInfo info : crafts) {
            result.append(Text.literal(" "));

            if (LegendwatchConfig.iconsEnabled.get()) {
                // Icons mode: show icon (or gold name fallback), with a gray "?" suffix if predicted
                result.append(LegendaryIcons.getDisplay(info.itemName));
                if (info.predicted) {
                    result.append(Text.literal("?").formatted(Formatting.GRAY));
                }
            } else {
                // Names mode: confirmed = gold, predicted = italic gray with "?" suffix
                if (info.predicted) {
                    result.append(Text.literal(info.itemName + "?")
                            .formatted(Formatting.GRAY, Formatting.ITALIC));
                } else {
                    result.append(Text.literal(info.itemName).formatted(Formatting.GOLD));
                }
            }
        }

        return result;
    }
}
