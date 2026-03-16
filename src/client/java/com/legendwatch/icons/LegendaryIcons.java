package com.legendwatch.icons;

import net.minecraft.text.Text;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Map;

public class LegendaryIcons {

    private static final StyleSpriteSource ICON_FONT =
            new StyleSpriteSource.Font(Identifier.of("legendwatch", "icons"));

    // Sprite sheet is 7 cols x 7 rows, icons ordered left-to-right top-to-bottom
    // matching the original inventory screenshot order (14 per row, 3 rows + 7)
    private static final Map<String, String> ICON_MAP = Map.ofEntries(
        Map.entry("Midas Sword",              "\uE000"),
        Map.entry("Artemis Bow",              "\uE001"),
        Map.entry("Reaper Scythe",            "\uE002"),
        Map.entry("Sonic Crossbow",           "\uE003"),
        Map.entry("Dragon Katana",            "\uE004"),
        Map.entry("Pufferfish Cannon",        "\uE005"),
        Map.entry("Emerald Blade",            "\uE006"),
        Map.entry("Magma Club",               "\uE007"),
        Map.entry("War Pick",                 "\uE008"),
        Map.entry("Aiglos",                   "\uE009"),
        Map.entry("Mjolnir",                  "\uE00A"),
        Map.entry("Fountain of Fate",         "\uE00B"),
        Map.entry("Shadow Blade",             "\uE00C"),
        Map.entry("Enderbow",                 "\uE00D"),
        Map.entry("Void Staff",               "\uE00E"),
        Map.entry("Lich Staff",               "\uE00F"),
        Map.entry("Reinforced Elytra",        "\uE010"),
        Map.entry("Poseidon's Trident",       "\uE011"),
        Map.entry("Ravager Horn",             "\uE012"),
        Map.entry("Beehive Blaster",          "\uE013"),
        Map.entry("Hypnosis Staff",           "\uE014"),
        Map.entry("Wither Sickles",           "\uE015"),
        Map.entry("Gerald the Sniffer",       "\uE016"),
        Map.entry("Jim the Sorcerer",         "\uE017"),
        Map.entry("Guardian Cannon",          "\uE018"),
        Map.entry("Cloud Sword",              "\uE019"),
        Map.entry("Horn of Winter",           "\uE01A"),
        Map.entry("Corrupted Crossbow",       "\uE01B"),
        Map.entry("Harpoon Launcher",         "\uE01C"),
        Map.entry("Shrink Ray",               "\uE01D"),
        Map.entry("Excalibur",                "\uE01E"),
        Map.entry("Golem Hammer",             "\uE01F"),
        Map.entry("Evoker Wand",              "\uE020"),
        Map.entry("Sculkweaver's Lantern",    "\uE021"),
        Map.entry("Armadillo Detonator",      "\uE022"),
        Map.entry("Kim the Transmuter",       "\uE023"),
        Map.entry("Soul Gauntlet",            "\uE024"),
        Map.entry("Phantom Longbow",          "\uE025"),
        Map.entry("Chrono Sword",             "\uE026"),
        Map.entry("Ribbit Reel",              "\uE027"),
        Map.entry("Eagle Eye Bow",            "\uE028"),
        Map.entry("Death Note",               "\uE029"),
        Map.entry("Magma Cannon",             "\uE02A"),
        Map.entry("Villager Wand",            "\uE02B"),
        Map.entry("Ghastly Whistle",          "\uE02C"),
        Map.entry("Gruntilda",                "\uE02D"),
        Map.entry("Headhunter's Chestpiece",  "\uE02E"),
        Map.entry("Crimson Chainsword",       "\uE02F"),
        Map.entry("Freezing Chakram",         "\uE030")
    );

    public static Text getDisplay(String itemName) {
        String codepoint = ICON_MAP.get(itemName);
        if (codepoint != null) {
            return Text.literal(codepoint)
                    .styled(style -> style.withFont(ICON_FONT)
                                         .withColor(0xFFFFFF));
        }
        return Text.literal(itemName).formatted(Formatting.GOLD);
    }

    public static boolean hasIcon(String itemName) {
        return ICON_MAP.containsKey(itemName);
    }
}
