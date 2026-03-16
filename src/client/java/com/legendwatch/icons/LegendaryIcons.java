package com.legendwatch.icons;

import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Map;

public class LegendaryIcons {

    private static final StyleSpriteSource ICON_FONT =
            new StyleSpriteSource.Font(Identifier.of("legendwatch", "icons"));

    // Maps the exact item name from the Hoplite chat message to a PUA codepoint.
    // If an item is not in this map, the plain name is shown instead.
    private static final Map<String, String> ICON_MAP = Map.ofEntries(
        Map.entry("Aiglos",             "\uE000"),
        Map.entry("Artemis Bow",       "\uE001"),
        Map.entry("Beehive Blaster",    "\uE002"),
        Map.entry("Dragon Katana",      "\uE003"),
        Map.entry("Emerald Blade",      "\uE004"),
        Map.entry("Enderbow",           "\uE005"),
        Map.entry("Hypnosis Staff",     "\uE006"),
        Map.entry("Lich Staff",         "\uE007"),
        Map.entry("Magma Club",         "\uE008"),
        Map.entry("Midas Sword",       "\uE009"),
        Map.entry("Mjolnir",            "\uE00A"),
        Map.entry("Poseidon's Trident", "\uE00B"),
        Map.entry("Pufferfish Cannon",  "\uE00C"),
        Map.entry("Ravager Horn",       "\uE00D"),
        Map.entry("Reaper's Scythe",    "\uE00E"),
        Map.entry("Shadow Blade",       "\uE00F"),
        Map.entry("Sonic Crossbow",     "\uE010"),
        Map.entry("Void Staff",         "\uE011"),
        Map.entry("War Pick",           "\uE012"),
        Map.entry("Wither Sickle",      "\uE013")
    );

    /**
     * Returns a Text representing this legendary — either a bitmap icon
     * (if one exists) or the plain item name as a fallback.
     */
    public static Text getDisplay(String itemName) {
        String codepoint = ICON_MAP.get(itemName);
        if (codepoint != null) {
            return Text.literal(codepoint)
                    .styled(style -> style.withFont(ICON_FONT)
                                         .withColor(0xFFFFFF));
        }
        // Fallback: plain gold text name
        return Text.literal(itemName).formatted(Formatting.GOLD);
    }

    public static boolean hasIcon(String itemName) {
        return ICON_MAP.containsKey(itemName);
    }
}
