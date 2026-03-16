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
                // Icons mode: bitmap glyph (or name fallback if no icon exists)
                result.append(LegendaryIcons.getDisplay(info.itemName));
            } else {
                // Names mode: always show plain gold text regardless of icon availability
                result.append(Text.literal(info.itemName).formatted(Formatting.GOLD));
            }
        }

        return result;
    }
}
