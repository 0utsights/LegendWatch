package com.legendwatch.util;

import com.legendwatch.LegendwatchConfig;
import com.legendwatch.icons.LegendaryIcons;
import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.tracker.LegendaryInfo;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;

public class LegendSuffixUtil {

    public static Text appendIfLegendary(Text original, String username) {
        if (!LegendwatchConfig.iconsEnabled.get()) return original;

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
