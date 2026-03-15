package com.legendwatch.util;

import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LegendSuffixUtil {

    /**
     * If the given username has a recorded legendary craft, returns the
     * original text with the item name appended in gold. Otherwise returns
     * the original text unchanged.
     */
    public static Text appendIfLegendary(Text original, String username) {
        LegendaryInfo info = CraftTracker.getCraft(username);
        if (info == null) return original;

        return Text.empty()
                .append(original)
                .append(Text.literal(" " + info.itemName)
                        .formatted(Formatting.GOLD));
    }
}
