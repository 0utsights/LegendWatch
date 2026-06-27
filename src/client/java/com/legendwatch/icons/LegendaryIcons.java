package com.legendwatch.icons;

import net.minecraft.text.Text;
//? if >=1.21.9 {
import net.minecraft.text.StyleSpriteSource;
//?}
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import com.legendwatch.LegendwatchConfig;

import java.util.Map;

public class LegendaryIcons {

    private static final Map<String, String> LEGENDARY_ALIASES = Map.ofEntries(
        Map.entry("Reinforced Elytra's Explosion", "Reinforced Elytra"),
        Map.entry("Summoned Ravager", "Ravager Horn"),
        Map.entry("Happy Ghast", "Ghastly Whistle"),
        Map.entry("Phantom Bow", "Phantom Longbow"),
        Map.entry("Headhunter's Might", "Headhunter's Chestpiece"),
        Map.entry("Dragonight Staff", "Dragon Sceptre"),
        Map.entry("Dragonflame Catalyst", "Dragon Sceptre")
    );

    private record LegendaryDefinition(String codepoint) {
        private boolean hasIcon() {
            return codepoint != null;
        }
    }

    //? if >=1.21.9 {
    private static final StyleSpriteSource ICON_FONT =
            new StyleSpriteSource.Font(Identifier.of("legendwatch", "icons"));
    private static final StyleSpriteSource TRANSPARENT_ICON_FONT =
            new StyleSpriteSource.Font(Identifier.of("legendwatch", "transparent_icons"));
    //?} else if >=1.21 {
    /*private static final Identifier ICON_FONT = Identifier.of("legendwatch", "icons");
    private static final Identifier TRANSPARENT_ICON_FONT = Identifier.of("legendwatch", "transparent_icons");*/
    //?} else {
    /*private static final Identifier ICON_FONT = new Identifier("legendwatch", "icons");
    private static final Identifier TRANSPARENT_ICON_FONT = new Identifier("legendwatch", "transparent_icons");*/
    //?}

    private static final Map<String, LegendaryDefinition> LEGENDARY_MAP = Map.ofEntries(
        Map.entry("Emerald Blade",            new LegendaryDefinition("\uE000")),
        Map.entry("Aiglos",                   new LegendaryDefinition("\uE001")),
        Map.entry("Armadillo Detonator",      new LegendaryDefinition("\uE002")),
        Map.entry("Artemis Bow",              new LegendaryDefinition("\uE003")),
        Map.entry("Beehive Blaster",          new LegendaryDefinition("\uE004")),
        Map.entry("Crimson Chainsword",       new LegendaryDefinition("\uE005")),
        Map.entry("Cloud Sword",              new LegendaryDefinition("\uE006")),
        Map.entry("Corrupted Crossbow",       new LegendaryDefinition("\uE007")),
        Map.entry("Death Note",               new LegendaryDefinition("\uE008")),
        Map.entry("Reinforced Elytra",        new LegendaryDefinition("\uE009")),
        Map.entry("Horn of Winter",           new LegendaryDefinition("\uE00A")),
        Map.entry("Enderbow",                 new LegendaryDefinition("\uE00B")),
        Map.entry("Evoker Wand",              new LegendaryDefinition("\uE00C")),
        Map.entry("Excalibur",                new LegendaryDefinition("\uE00D")),
        Map.entry("Gerald the Sniffer",       new LegendaryDefinition("\uE00E")),
        Map.entry("Ghastly Whistle",          new LegendaryDefinition("\uE00F")),
        Map.entry("Golem Hammer",             new LegendaryDefinition("\uE010")),
        Map.entry("Gruntilda",                new LegendaryDefinition("\uE011")),
        Map.entry("Guardian Cannon",          new LegendaryDefinition("\uE012")),
        Map.entry("Harpoon Launcher",         new LegendaryDefinition("\uE013")),
        Map.entry("Phantom Longbow",          new LegendaryDefinition("\uE014")),
        Map.entry("Hypnosis Staff",           new LegendaryDefinition("\uE015")),
        Map.entry("Jim the Sorcerer",         new LegendaryDefinition("\uE016")),
        Map.entry("Dragon Katana",            new LegendaryDefinition("\uE017")),
        Map.entry("Kim the Transmuter",       new LegendaryDefinition("\uE018")),
        Map.entry("Sculkweaver's Lantern",    new LegendaryDefinition("\uE019")),
        Map.entry("Lich Staff",               new LegendaryDefinition("\uE01A")),
        Map.entry("Magma Club",               new LegendaryDefinition("\uE01B")),
        Map.entry("Midas Sword",              new LegendaryDefinition("\uE01C")),
        Map.entry("Mjolnir",                  new LegendaryDefinition("\uE01D")),
        Map.entry("Poseidon's Trident",       new LegendaryDefinition("\uE01E")),
        Map.entry("Pufferfish Cannon",        new LegendaryDefinition("\uE01F")),
        Map.entry("Ravager Horn",             new LegendaryDefinition("\uE020")),
        Map.entry("Reaper Scythe",            new LegendaryDefinition("\uE021")),
        Map.entry("Ribbit Reel",              new LegendaryDefinition("\uE022")),
        Map.entry("Shadow Blade",             new LegendaryDefinition("\uE023")),
        Map.entry("Shrink Ray",               new LegendaryDefinition("\uE024")),
        Map.entry("Sonic Crossbow",           new LegendaryDefinition("\uE025")),
        Map.entry("Soul Gauntlet",            new LegendaryDefinition("\uE026")),
        Map.entry("Villager Wand",            new LegendaryDefinition("\uE027")),
        Map.entry("Void Staff",               new LegendaryDefinition("\uE028")),
        Map.entry("War Pick",                 new LegendaryDefinition("\uE029")),
        Map.entry("Wither Sickles",           new LegendaryDefinition("\uE02A")),
        Map.entry("Eagle Eye Bow",            new LegendaryDefinition("\uE02B")),
        Map.entry("Freezing Chakram",         new LegendaryDefinition("\uE02C")),
        Map.entry("Headhunter's Chestpiece",  new LegendaryDefinition("\uE02D")),
        Map.entry("Magma Cannon",             new LegendaryDefinition("\uE02E")),
        Map.entry("Chrono Sword",             new LegendaryDefinition("\uE02F")),
        Map.entry("Sakura Tessen",            new LegendaryDefinition("\uE030")),
        Map.entry("Elder Eye of Possession",  new LegendaryDefinition("\uE031")),
        Map.entry("Dragon Sceptre",           new LegendaryDefinition("\uE032")),
        Map.entry("Sceptre of Arachne",       new LegendaryDefinition("\uE033")),
        Map.entry("Vampire Sabre",            new LegendaryDefinition("\uE034"))
    );

    public static Text getDisplay(String itemName) {
        LegendaryDefinition legendary = LEGENDARY_MAP.get(itemName);
        if (legendary != null && legendary.hasIcon()) {
            //? if >=1.21.9 {
            StyleSpriteSource font = LegendwatchConfig.transparentIconsEnabled.get()
                    ? TRANSPARENT_ICON_FONT : ICON_FONT;
            //?} else {
            /*Identifier font = LegendwatchConfig.transparentIconsEnabled.get()
                    ? TRANSPARENT_ICON_FONT : ICON_FONT;*/
            //?}
            return Text.literal(legendary.codepoint())
                    .styled(style -> style.withFont(font)
                                         .withColor(0xFFFFFF));
        }
        return Text.literal(itemName).formatted(Formatting.GOLD);
    }

    public static boolean hasIcon(String itemName) {
        LegendaryDefinition legendary = LEGENDARY_MAP.get(itemName);
        return legendary != null && legendary.hasIcon();
    }

    public static boolean isKnownLegendary(String itemName) {
        return LEGENDARY_MAP.containsKey(itemName);
    }

    public static String getCanonicalName(String itemName) {
        if (itemName == null) return null;
        if (LEGENDARY_MAP.containsKey(itemName)) return itemName;
        return LEGENDARY_ALIASES.get(itemName);
    }
}
