package com.legendwatch.listener;

import com.legendwatch.tracker.CraftTracker;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {

    private static final Pattern CRAFT_PATTERN = Pattern.compile(
            "^(.+?) has crafted the (.+?)! This legendary cannot be crafted again!$"
    );

    private static final Pattern ELIMINATION_PATTERN = Pattern.compile(
            "^ELIMINATION! (.+?) was slain by (.+?)$"
    );

    private static final Pattern[] RESET_PATTERNS = new Pattern[]{
            Pattern.compile(".*has joined.*")
    };

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
        String clean = raw.replaceAll("§[0-9a-fk-or]", "")
                          .replaceAll("[^\\x00-\\x7F]", "")
                          .trim();

        // Ignore player chat messages — they contain a colon (e.g. "Username: hello")
        // This prevents players from spoofing reset triggers
        if (clean.contains(":")) return;

        // Check for match reset triggers
        for (Pattern reset : RESET_PATTERNS) {
            if (reset.matcher(clean).matches()) {
                CraftTracker.onMatchReset();
                return;
            }
        }

        // Check for player elimination — transfer slain's legendaries to slayer as predicted
        Matcher elimMatcher = ELIMINATION_PATTERN.matcher(clean);
        if (elimMatcher.matches()) {
            String slainGroup = elimMatcher.group(1).trim();
            String slain = slainGroup.contains(" ")
                    ? slainGroup.substring(slainGroup.lastIndexOf(" ") + 1)
                    : slainGroup;

            String slayerGroup = elimMatcher.group(2).trim();
            String slayer = slayerGroup.contains(" ")
                    ? slayerGroup.substring(slayerGroup.lastIndexOf(" ") + 1)
                    : slayerGroup;

            CraftTracker.onPlayerEliminated(slain, slayer);
            return;
        }

        // Check for legendary craft announcement
        Matcher matcher = CRAFT_PATTERN.matcher(clean);
        if (matcher.matches()) {
            // Group 1 may be "[RANK] Username" or just "Username"
            // Take only the last word (the actual username)
            String group = matcher.group(1).trim();
            String username = group.contains(" ")
                    ? group.substring(group.lastIndexOf(" ") + 1)
                    : group;

            String itemName = matcher.group(2).trim();
            CraftTracker.recordCraft(username, itemName);

            net.minecraft.client.MinecraftClient.getInstance().player.sendMessage(
                    Text.literal("§a[LegendWatch] Recorded: §f" + username + " §7→ §6" + itemName), false);
        }
    }
}
