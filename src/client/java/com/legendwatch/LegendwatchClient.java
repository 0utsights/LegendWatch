package com.legendwatch;

import com.legendwatch.listener.ChatListener;
import com.legendwatch.tracker.CraftTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
//? if >=1.21.9 {
import net.minecraft.util.Identifier;
//?}
import net.minecraft.util.Formatting;
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
        LegendwatchConfig.load();
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
                LegendwatchConfig.save();
                if (client.player != null) {
                    client.player.sendMessage(statusMessage(
                            "Mod",
                            nowEnabled ? "Enabled" : "Disabled",
                            nowEnabled ? Formatting.GREEN : Formatting.RED
                    ), false);
                }
            }

            while (toggleIconsKey.wasPressed()) {
                boolean nowEnabled = !LegendwatchConfig.iconsEnabled.get();
                LegendwatchConfig.iconsEnabled.set(nowEnabled);
                LegendwatchConfig.save();
                if (client.player != null) {
                    client.player.sendMessage(statusMessage(
                            "Display mode",
                            nowEnabled ? "Icons" : "Names only",
                            nowEnabled ? Formatting.GREEN : Formatting.YELLOW
                    ), false);
                }
            }

            while (togglePredictedKey.wasPressed()) {
                boolean nowEnabled = !LegendwatchConfig.predictedEnabled.get();
                LegendwatchConfig.predictedEnabled.set(nowEnabled);
                LegendwatchConfig.save();
                if (client.player != null) {
                    client.player.sendMessage(statusMessage(
                            "Predicted legendaries",
                            nowEnabled ? "Shown" : "Hidden",
                            nowEnabled ? Formatting.GREEN : Formatting.RED
                    ), false);
                }
            }

            while (toggleTransparentKey.wasPressed()) {
                boolean nowEnabled = !LegendwatchConfig.transparentIconsEnabled.get();
                LegendwatchConfig.transparentIconsEnabled.set(nowEnabled);
                LegendwatchConfig.save();
                if (client.player != null) {
                    client.player.sendMessage(statusMessage(
                            "Icon style",
                            nowEnabled ? "Transparent" : "Solid",
                            nowEnabled ? Formatting.GREEN : Formatting.GRAY
                    ), false);
                }
            }
        });
    }

    private static Text statusMessage(String setting, String value, Formatting valueFormatting) {
        return Text.literal("[LegendWatch] " + setting + ": ")
                .formatted(Formatting.GOLD)
                .append(Text.literal(value).formatted(valueFormatting));
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
