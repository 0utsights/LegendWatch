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

    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        CraftTracker.init();
        ChatListener.init();

        // 1.21.9+ requires a KeyBinding.Category object instead of a plain string
        KeyBinding.Category category = KeyBinding.Category.register(
                Identifier.of("legendwatch", "general")
        );

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.legendwatch.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                boolean nowEnabled = !LegendwatchConfig.iconsEnabled.get();
                LegendwatchConfig.iconsEnabled.set(nowEnabled);

                if (client.player != null) {
                    String state = nowEnabled ? "§aEnabled" : "§cDisabled";
                    client.player.sendMessage(
                            Text.literal("§6[LegendWatch] §fLegendary icons: " + state),
                            false
                    );
                }
            }
        });
    }
}
