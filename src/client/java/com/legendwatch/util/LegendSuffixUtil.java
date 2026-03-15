package com.legendwatch.util;

import com.legendwatch.icons.LegendaryIcons;
import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
import net.minecraft.text.Text;

public class LegendSuffixUtil {

    /**
     * If the given username has a recorded legendary craft, appends either:
     *  - a bitmap icon (if one exists for that item), or
     *  - the plain item name in gold (fallback)
     * Otherwise returns the original text unchanged.
     */
    public static Text appendIfLegendary(Text original, String username) {
        LegendaryInfo info = CraftTracker.getCraft(username);
        if (info == null) return original;

        return Text.empty()
                .append(original)
                .append(Text.literal(" "))
                .append(LegendaryIcons.getDisplay(info.itemName));
    }
}
