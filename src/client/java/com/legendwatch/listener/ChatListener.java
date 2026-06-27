package com.legendwatch.listener;

import com.legendwatch.LegendwatchConfig;
import com.legendwatch.icons.LegendaryIcons;
import com.legendwatch.tracker.CraftTracker;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {

    private static final Pattern CRAFT_PATTERN = Pattern.compile(
            "^(.+?) has crafted the (.+?)! This legendary cannot be crafted again!$"
    );

    private static final Pattern ELIMINATION_PATTERN = Pattern.compile(
            "^ELIMINATION! (.+?)(?:'s (?:mind|life|soul))? (?:was|has|had) (.+)$"
    );

    private static final Pattern KILLER_WEAPON_PATTERN = Pattern.compile(
            "^.+(?:by|from) ([^ ]+)'s (.+)$"
    );

    private static final Pattern DEATH_NOTE_PATTERN = Pattern.compile(
            "^.+using the Death Note\\.?$"
    );

    private static final Pattern[] RESET_PATTERNS = new Pattern[]{
            Pattern.compile(".*has joined.*")
    };

    private static final List<LegendaryKillPattern> EXACT_LEGENDARY_KILL_PATTERNS = List.of(
            exactLegendaryKill(" was sliced up by ", "Dragon Katana"),
            exactLegendaryKill(" was blown apart by ", "Reinforced Elytra's Explosion", "Reinforced Elytra"),
            exactLegendaryKill(" was turned to gold by ", "Midas Sword"),
            exactLegendaryKill(" was electrified by ", "Mjolnir"),
            exactLegendaryKill(" had their soul absorbed by ", "Reaper Scythe"),
            exactLegendaryKill(" was obliterated by a shockwave from ", "Sonic Crossbow"),
            exactLegendaryKill(" was bludgeoned by ", "War Pick"),
            exactLegendaryKill(" was banished to The End by ", "Enderbow"),
            exactLegendaryKill(" was tracked down by ", "Artemis Bow"),
            exactLegendaryKill(" was poked to death by ", "Pufferfish Cannon"),
            exactLegendaryKill(" was stomped by ", "Summoned Ravager", "Ravager Horn"),
            exactLegendaryKill(" was frozen solid by ", "Lich Staff"),
            exactLegendaryKill(" was dazzled by ", "Emerald Blade"),
            exactLegendaryKill(" was clobbered by ", "Void Staff"),
            exactLegendaryKill(" was bashed by ", "Magma Club"),
            exactLegendaryKill(" was sent to the shadow realm by ", "Shadow Blade"),
            exactLegendaryKill(" was impaled by ", "Aiglos"),
            exactLegendaryKill("'s mind was corrupted by ", "Phantom Bow", "Phantom Longbow"),
            exactLegendaryKill(" was swooped down upon by ", "Eagle Eye Bow"),
            exactLegendaryKill(" was charbroiled by ", "Magma Cannon"),
            exactLegendaryKill(" was erased from history by ", "Chrono Sword"),
            exactLegendaryKill(" was shredded to bits by ", "Freezing Chakram"),
            exactLegendaryKill(" was torn apart with fury by ", "Headhunter's Might",
                    "Headhunter's Chestpiece"),
            exactLegendaryKill(" was scattered into petals by ", "Sakura Tessen"),
            exactLegendaryKill(" was woven into a web by ", "Sceptre of Arachne"),
            exactLegendaryKill(" was reduced to ash by ", "Dragonight Staff", "Dragon Sceptre"),
            exactLegendaryKill("'s life was siphoned away by ", "Vampire Sabre"),
            exactLegendaryKill(" was stared to death by ", "Elder Eye of Possession"),
            exactLegendaryKill(" was transmuted to dust by ", "Kim the Transmuter"),
            exactLegendaryKill(" was shredded to bits by ", "Crimson Chainsword"),
            exactLegendaryKill(" was detonated by ", "Armadillo Detonator"),
            exactLegendaryKill(" was banished to the Deep Dark by ", "Sculkweaver's Lantern"),
            exactLegendaryKill(" was clobbered by ", "Hypnosis Staff"),
            exactLegendaryKill(" was impaled by ", "Poseidon's Trident"),
            exactLegendaryKill(" was knocked out by ", "Beehive Blaster"),
            exactLegendaryKill(" was sliced to pieces by ", "Wither Sickles"),
            exactLegendaryKill("'s soul was captured by ", "Soul Gauntlet"),
            exactLegendaryKill(" was bedazzled by ", "Villager Wand"),
            exactLegendaryKill(" was lasered by ", "Guardian Cannon"),
            exactLegendaryKill(" was blown up by ", "Happy Ghast", "Ghastly Whistle"),
            exactLegendaryKill(" was slashed up by ", "Cloud Sword"),
            exactLegendaryKill(" was infected by ", "Corrupted Crossbow"),
            exactLegendaryKill(" was turned to ice by ", "Horn of Winter"),
            exactLegendaryKill(" was pulled apart by ", "Harpoon Launcher"),
            exactLegendaryKill(" was overwhelmed by ", "Excalibur"),
            exactLegendaryKill(" was consumed by ", "Evoker Wand"),
            exactLegendaryKill(" was shrunk to atoms by ", "Shrink Ray"),
            exactLegendaryKill(" was smashed to pieces by ", "Golem Hammer"),
            exactLegendaryKill(" was slurped up by ", "Ribbit Reel")
    );

    private static final Pattern EXACT_DEATH_NOTE_PATTERN = Pattern.compile(
            "^ELIMINATION! (.+?) was assassinated by a player using the Death Note\\.$"
    );

    private record EliminationEvent(String slain, String slayer, String observedLegendaryName) {
    }

    private record LegendaryKillPattern(Pattern pattern, String observedLegendaryName) {
    }

    public static void init() {
        ClientReceiveMessageEvents.GAME.register(ChatListener::onGameMessage);
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) ->
                onGameMessage(message, false)
        );
    }

    private static void onGameMessage(Text message, boolean overlay) {
        if (overlay) return;

        String raw = message.getString();
        // Strip Minecraft color codes AND any non-ASCII characters (rank glyphs, PUA icons)
        String clean = raw.replaceAll("\u00A7[0-9a-fk-or]", "")
                          .replaceAll("[^\\x00-\\x7F]", "")
                          .trim();

        // Ignore player chat messages - they contain a colon (e.g. "Username: hello")
        // This prevents players from spoofing reset triggers
        if (clean.contains(":")) return;

        // Check for match reset triggers
        for (Pattern reset : RESET_PATTERNS) {
            if (reset.matcher(clean).matches()) {
                CraftTracker.onMatchReset();
                return;
            }
        }

        // Check for player elimination - transfer slain's legendaries to slayer as predicted.
        // If the kill message also reveals a legendary weapon, use that observation to confirm
        // or infer ownership for the slayer.
        EliminationEvent exactLegendaryElimination = parseExactLegendaryElimination(clean);
        if (exactLegendaryElimination != null) {
            CraftTracker.onPlayerEliminated(exactLegendaryElimination.slain(), exactLegendaryElimination.slayer());

            if (exactLegendaryElimination.observedLegendaryName() != null) {
                CraftTracker.onLegendaryKillObserved(
                        exactLegendaryElimination.slayer(),
                        exactLegendaryElimination.observedLegendaryName(),
                        LegendwatchConfig.experimentalGeraldTrackingEnabled.get()
                );
            }
            return;
        }

        Matcher elimMatcher = ELIMINATION_PATTERN.matcher(clean);
        if (elimMatcher.matches()) {
            EliminationEvent elimination = parseElimination(elimMatcher);
            CraftTracker.onPlayerEliminated(elimination.slain(), elimination.slayer());

            if (elimination.observedLegendaryName() != null) {
                CraftTracker.onLegendaryKillObserved(
                        elimination.slayer(),
                        elimination.observedLegendaryName(),
                        LegendwatchConfig.experimentalGeraldTrackingEnabled.get()
                );
            }
            return;
        }

        // Check for legendary craft announcement
        Matcher matcher = CRAFT_PATTERN.matcher(clean);
        if (matcher.matches()) {
            String username = extractUsername(matcher.group(1).trim());

            String rawItemName = matcher.group(2).trim();
            String itemName = canonicalizeLegendaryName(rawItemName);
            if (itemName == null) {
                itemName = rawItemName;
            }

            CraftTracker.recordCraft(username, itemName);

            net.minecraft.client.MinecraftClient.getInstance().player.sendMessage(
                    Text.literal("\u00A7a[LegendWatch] Recorded: \u00A7f"
                            + username + " \u00A77-> \u00A76" + itemName), false);
        }
    }

    private static EliminationEvent parseExactLegendaryElimination(String clean) {
        for (LegendaryKillPattern killPattern : EXACT_LEGENDARY_KILL_PATTERNS) {
            Matcher matcher = killPattern.pattern().matcher(clean);
            if (!matcher.matches()) continue;

            String slain = extractUsername(matcher.group(1).trim());
            String slayer = stripTrailingPunctuation(matcher.group(2).trim());
            return new EliminationEvent(slain, slayer, killPattern.observedLegendaryName());
        }

        Matcher deathNoteMatcher = EXACT_DEATH_NOTE_PATTERN.matcher(clean);
        if (deathNoteMatcher.matches()) {
            return new EliminationEvent(
                    extractUsername(deathNoteMatcher.group(1).trim()),
                    null,
                    null
            );
        }

        return null;
    }

    private static EliminationEvent parseElimination(Matcher elimMatcher) {
        String slain = extractUsername(elimMatcher.group(1).trim());
        String remainder = elimMatcher.group(2).trim();

        Matcher killerWeaponMatcher = KILLER_WEAPON_PATTERN.matcher(remainder);
        if (killerWeaponMatcher.matches()) {
            String slayer = stripTrailingPunctuation(killerWeaponMatcher.group(1).trim());
            String weaponText = stripTrailingPunctuation(killerWeaponMatcher.group(2).trim());
            return new EliminationEvent(slain, slayer, canonicalizeLegendaryName(weaponText));
        }

        if (DEATH_NOTE_PATTERN.matcher(remainder).matches()) {
            return new EliminationEvent(slain, null, null);
        }

        String[] words = remainder.split(" ");
        String slayer = stripTrailingPunctuation(words[words.length - 1]);
        return new EliminationEvent(slain, slayer, null);
    }

    private static String canonicalizeLegendaryName(String itemName) {
        return LegendaryIcons.getCanonicalName(itemName);
    }

    private static String extractUsername(String text) {
        return text.contains(" ")
                ? text.substring(text.lastIndexOf(" ") + 1)
                : text;
    }

    private static String stripTrailingPunctuation(String text) {
        return text.replaceAll("[.!]+$", "").trim();
    }

    private static LegendaryKillPattern exactLegendaryKill(String victimToAction, String itemName) {
        return exactLegendaryKill(victimToAction, itemName, itemName);
    }

    private static LegendaryKillPattern exactLegendaryKill(String victimToAction, String visibleItemName,
                                                           String canonicalItemName) {
        Pattern pattern = Pattern.compile(
                "^ELIMINATION! (.+?)"
                        + Pattern.quote(victimToAction)
                        + "([^ ]+)'s "
                        + Pattern.quote(visibleItemName)
                        + "$"
        );
        return new LegendaryKillPattern(pattern, canonicalItemName);
    }
}
