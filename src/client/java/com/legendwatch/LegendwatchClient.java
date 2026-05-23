package com.legendwatch;

import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.listener.ChatListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
//? if >=1.21.9 {
import net.minecraft.util.Identifier;
//?}
import org.lwjgl.glfw.GLFW;

public class LegendwatchClient implements ClientModInitializer {

    private static KeyBinding toggleModKey;
    private static KeyBinding toggleIconsKey;
    private static KeyBinding togglePredictedKey;
    private static KeyBinding toggleTransparentKey;
    //? if >=1.21.9 {
    private static final KeyBinding.Category KEY_CATEGORY =
            KeyBinding.Category.create(Identifier.of("legendwatch", "general"));
    //?}

    @Override
    public void onInitializeClient() {
        CraftTracker.init();
        ChatListener.init();

        toggleModKey = registerKeyBinding("key.legendwatch.toggle_mod");
        toggleIconsKey = registerKeyBinding("key.legendwatch.toggle_icons");
        togglePredictedKey = registerKeyBinding("key.legendwatch.toggle_predicted");
        toggleTransparentKey = registerKeyBinding("key.legendwatch.toggle_transparent");

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

            while (togglePredictedKey.wasPressed()) {
                boolean nowEnabled = !LegendwatchConfig.predictedEnabled.get();
                LegendwatchConfig.predictedEnabled.set(nowEnabled);
                if (client.player != null) {
                    String state = nowEnabled ? "§aShown" : "§cHidden";
                    client.player.sendMessage(
                            Text.literal("§6[LegendWatch] §fPredicted legendaries: " + state), false);
                }
            }

            while (toggleTransparentKey.wasPressed()) {
                boolean nowEnabled = !LegendwatchConfig.transparentIconsEnabled.get();
                LegendwatchConfig.transparentIconsEnabled.set(nowEnabled);
                if (client.player != null) {
                    String state = nowEnabled ? "§aTransparent" : "§7Solid";
                    client.player.sendMessage(
                            Text.literal("§6[LegendWatch] §fIcon style: " + state), false);
                }
            }
        });
    }

    private static KeyBinding registerKeyBinding(String translationKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                translationKey,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                //? if >=1.21.9 {
                KEY_CATEGORY
                //?} else {
                /*"key.category.legendwatch.general"*/
                //?}
        ));
    }
}
