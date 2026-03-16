package com.legendwatch.util;

import com.legendwatch.icons.LegendaryIcons;
import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;

public class LegendSuffixUtil {

    /**
     * If the given username has recorded legendary crafts, appends each one
     * (icon or name) separated by a space. Returns original text unchanged
     * if no crafts are recorded.
     */
    public static Text appendIfLegendary(Text original, String username) {
        List<LegendaryInfo> crafts = CraftTracker.getCrafts(username);
        if (crafts.isEmpty()) return original;

        MutableText result = Text.empty().append(original);

        for (LegendaryInfo info : crafts) {
            result.append(Text.literal(" "))
                  .append(LegendaryIcons.getDisplay(info.itemName));
        }

        return result;
    }
}
