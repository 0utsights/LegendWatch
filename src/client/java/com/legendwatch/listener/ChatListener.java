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

    private static final Pattern[] RESET_PATTERNS = new Pattern[]{
            Pattern.compile(".*Sending you to.*"),
            Pattern.compile(".*Returning to lobby.*"),
            Pattern.compile(".*Match has ended.*"),
            Pattern.compile(".*Game over.*"),
            Pattern.compile(".*You have been sent to.*")
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
        String clean = raw.replaceAll("§[0-9a-fk-or]", "").trim();

        // Check for match reset triggers
        for (Pattern reset : RESET_PATTERNS) {
            if (reset.matcher(clean).matches()) {
                CraftTracker.onMatchReset();
                return;
            }
        }

        // Check for legendary craft announcement
        Matcher matcher = CRAFT_PATTERN.matcher(clean);
        if (matcher.matches()) {
            String raw_username = matcher.group(1).trim().replaceAll("^\\[|\\]$", "");
            String username = raw_username.contains(" ") ? raw_username.substring(raw_username.lastIndexOf(" ") + 1) : raw_username;
            String itemName = matcher.group(2).trim();
            CraftTracker.recordCraft(username, itemName);
            net.minecraft.client.MinecraftClient.getInstance().player.sendMessage(
                    net.minecraft.text.Text.literal("§a[LegendWatch] Recorded: " + username + " → " + itemName), false);
        }
    }
}