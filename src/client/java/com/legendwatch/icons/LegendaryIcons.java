package com.legendwatch.icons;

import net.minecraft.text.Text;
//? if >=1.21.9 {
import net.minecraft.text.StyleSpriteSource;
//?}
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Map;

public class LegendaryIcons {

    //? if >=1.21.9 {
    private static final StyleSpriteSource ICON_FONT =
            new StyleSpriteSource.Font(Identifier.of("legendwatch", "icons"));
    //?} else if >=1.21 {
    /*private static final Identifier ICON_FONT = Identifier.of("legendwatch", "icons");*/
    //?} else {
    /*private static final Identifier ICON_FONT = new Identifier("legendwatch", "icons");*/
    //?}

    private static final Map<String, String> ICON_MAP = Map.ofEntries(
        Map.entry("Emerald Blade",            "\uE000"),
        Map.entry("Aiglos",                   "\uE001"),
        Map.entry("Armadillo Detonator",      "\uE002"),
        Map.entry("Artemis Bow",              "\uE003"),
        Map.entry("Beehive Blaster",          "\uE004"),
        Map.entry("Crimson Chainsword",       "\uE005"),
        Map.entry("Cloud Sword",              "\uE006"),
        Map.entry("Corrupted Crossbow",       "\uE007"),
        Map.entry("Death Note",               "\uE008"),
        Map.entry("Reinforced Elytra",        "\uE009"),
        Map.entry("Horn of Winter",           "\uE00A"),
        Map.entry("Enderbow",                 "\uE00B"),
        Map.entry("Evoker Wand",              "\uE00C"),
        Map.entry("Excalibur",                "\uE00D"),
        Map.entry("Gerald the Sniffer",       "\uE00E"),
        Map.entry("Ghastly Whistle",          "\uE00F"),
        Map.entry("Golem Hammer",             "\uE010"),
        Map.entry("Gruntilda",                "\uE011"),
        Map.entry("Guardian Cannon",          "\uE012"),
        Map.entry("Harpoon Launcher",         "\uE013"),
        Map.entry("Phantom Longbow",          "\uE014"),
        Map.entry("Hypnosis Staff",           "\uE015"),
        Map.entry("Jim the Sorcerer",         "\uE016"),
        Map.entry("Dragon Katana",            "\uE017"),
        Map.entry("Kim the Transmuter",       "\uE018"),
        Map.entry("Sculkweaver's Lantern",    "\uE019"),
        Map.entry("Lich Staff",               "\uE01A"),
        Map.entry("Magma Club",               "\uE01B"),
        Map.entry("Midas Sword",              "\uE01C"),
        Map.entry("Mjolnir",                  "\uE01D"),
        Map.entry("Poseidon's Trident",       "\uE01E"),
        Map.entry("Pufferfish Cannon",        "\uE01F"),
        Map.entry("Ravager Horn",             "\uE020"),
        Map.entry("Reaper Scythe",            "\uE021"),
        Map.entry("Ribbit Reel",              "\uE022"),
        Map.entry("Shadow Blade",             "\uE023"),
        Map.entry("Shrink Ray",               "\uE024"),
        Map.entry("Sonic Crossbow",           "\uE025"),
        Map.entry("Soul Gauntlet",            "\uE026"),
        Map.entry("Villager Wand",            "\uE027"),
        Map.entry("Void Staff",               "\uE028"),
        Map.entry("War Pick",                 "\uE029"),
        Map.entry("Wither Sickles",           "\uE02A"),
        Map.entry("Eagle Eye Bow",            "\uE02B"),
        Map.entry("Freezing Chakram",         "\uE02C"),
        Map.entry("Headhunter's Chestpiece",  "\uE02D"),
        Map.entry("Magma Cannon",             "\uE02E"),
        Map.entry("Chrono Sword",             "\uE02F")
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
