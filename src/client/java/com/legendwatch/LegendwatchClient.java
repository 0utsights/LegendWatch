package com.legendwatch;

import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.listener.ChatListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class LegendwatchClient implements ClientModInitializer {

    private static KeyBinding toggleModKey;
    private static KeyBinding toggleIconsKey;

    @Override
    public void onInitializeClient() {
        CraftTracker.init();
        ChatListener.init();

        KeyBinding.Category category = KeyBinding.Category.create(
                Identifier.of("legendwatch", "general")
        );

        toggleModKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.legendwatch.toggle_mod",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        toggleIconsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.legendwatch.toggle_icons",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleModKey.wasPressed()) {
                boolean nowEnabled = !LegendwatchConfig.modEnabled.get();
                LegendwatchConfig.modEnabled.set(nowEnabled);
                if (client.player != null) {
                    String state = nowEnabled ? "§aEnabled" : "§cDisabled";
                    client.player.sendMessage(
                            Text.literal("§6[LegendWatch] §fMod: " + state), false);
                }
            }

            while (toggleIconsKey.wasPressed()) {
                boolean nowEnabled = !LegendwatchConfig.iconsEnabled.get();
                LegendwatchConfig.iconsEnabled.set(nowEnabled);
                if (client.player != null) {
                    String state = nowEnabled ? "§aIcons" : "§eNames only";
                    client.player.sendMessage(
                            Text.literal("§6[LegendWatch] §fDisplay mode: " + state), false);
                }
            }
        });
    }
}
