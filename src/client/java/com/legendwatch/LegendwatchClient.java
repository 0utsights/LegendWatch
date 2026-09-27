package com.legendwatch;

import com.legendwatch.listener.ChatListener;
import com.legendwatch.tracker.CraftTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >=26.1 {
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
//?} else if >=1.21.9 {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.network.ClientPlayerEntity;*/
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.client.network.ClientPlayerEntity;*/
//?}
import org.lwjgl.glfw.GLFW;

public class LegendwatchClient implements ClientModInitializer {

    //? if >=26.1 {
    private static KeyMapping toggleModKey;
    private static KeyMapping toggleIconsKey;
    private static KeyMapping togglePredictedKey;
    private static KeyMapping toggleTransparentKey;
    private static KeyMapping cycleMidasDisplayKey;
    private static final KeyMapping.Category KEY_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("legendwatch", "general"));
    //?} else if >=1.21.9 {
    /*private static KeyBinding toggleModKey;
    private static KeyBinding toggleIconsKey;
    private static KeyBinding togglePredictedKey;
    private static KeyBinding toggleTransparentKey;
    private static KeyBinding cycleMidasDisplayKey;
    private static final KeyBinding.Category KEY_CATEGORY =
            KeyBinding.Category.create(Identifier.of("legendwatch", "general"));*/
    //?} else {
    /*private static KeyBinding toggleModKey;
    private static KeyBinding toggleIconsKey;
    private static KeyBinding togglePredictedKey;
    private static KeyBinding toggleTransparentKey;
    private static KeyBinding cycleMidasDisplayKey;*/
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
        cycleMidasDisplayKey = registerKeyBinding("key.legendwatch.cycle_midas_display");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            //? if >=26.1 {
            while (toggleModKey.consumeClick()) {
            //?} else {
            /*while (toggleModKey.wasPressed()) {
            *///?}
                boolean nowEnabled = !LegendwatchConfig.modEnabled.get();
                LegendwatchConfig.modEnabled.set(nowEnabled);
                LegendwatchConfig.save();
                if (client.player != null) {
                    sendStatus(client.player,
                            "Mod",
                            nowEnabled ? "Enabled" : "Disabled",
                            nowEnabled ? 0x55FF55 : 0xFF5555
                    );
                }
            }

            //? if >=26.1 {
            while (toggleIconsKey.consumeClick()) {
            //?} else {
            /*while (toggleIconsKey.wasPressed()) {
            *///?}
                boolean nowEnabled = !LegendwatchConfig.iconsEnabled.get();
                LegendwatchConfig.iconsEnabled.set(nowEnabled);
                LegendwatchConfig.save();
                if (client.player != null) {
                    sendStatus(client.player,
                            "Display mode",
                            nowEnabled ? "Icons" : "Names only",
                            nowEnabled ? 0x55FF55 : 0xFFFF55
                    );
                }
            }

            //? if >=26.1 {
            while (togglePredictedKey.consumeClick()) {
            //?} else {
            /*while (togglePredictedKey.wasPressed()) {
            *///?}
                boolean nowEnabled = !LegendwatchConfig.predictedEnabled.get();
                LegendwatchConfig.predictedEnabled.set(nowEnabled);
                LegendwatchConfig.save();
                if (client.player != null) {
                    sendStatus(client.player,
                            "Predicted legendaries",
                            nowEnabled ? "Shown" : "Hidden",
                            nowEnabled ? 0x55FF55 : 0xFF5555
                    );
                }
            }

            //? if >=26.1 {
            while (toggleTransparentKey.consumeClick()) {
            //?} else {
            /*while (toggleTransparentKey.wasPressed()) {
            *///?}
                boolean nowEnabled = !LegendwatchConfig.transparentIconsEnabled.get();
                LegendwatchConfig.transparentIconsEnabled.set(nowEnabled);
                LegendwatchConfig.save();
                if (client.player != null) {
                    sendStatus(client.player,
                            "Icon style",
                            nowEnabled ? "Transparent" : "Solid",
                            nowEnabled ? 0x55FF55 : 0xAAAAAA
                    );
                }
            }

            //? if >=26.1 {
            while (cycleMidasDisplayKey.consumeClick()) {
            //?} else {
            /*while (cycleMidasDisplayKey.wasPressed()) {
            *///?}
                LegendwatchConfig.MidasDisplayMode nextMode =
                        LegendwatchConfig.midasDisplayMode.updateAndGet(LegendwatchConfig.MidasDisplayMode::next);
                LegendwatchConfig.save();
                if (client.player != null) {
                    sendStatus(client.player,
                            "Midas display",
                            nextMode.displayName(),
                            switch (nextMode) {
                                case SHARPNESS_ESTIMATE -> 0x55FFFF;
                                case KILL_COUNT -> 0x55FF55;
                                case OFF -> 0xAAAAAA;
                            }
                    );
                }
            }
        });
    }

    //? if >=26.1 {
    private static Component statusMessage(String setting, String value, int valueColor) {
        return Component.literal("[LegendWatch] " + setting + ": ")
                .withStyle(style -> style.withColor(0xFFAA00))
                .append(Component.literal(value).withStyle(style -> style.withColor(valueColor)));
    }

    private static void sendStatus(LocalPlayer player, String setting, String value, int valueColor) {
        player.sendSystemMessage(statusMessage(setting, value, valueColor));
    }
    //?} else {
    /*private static Text statusMessage(String setting, String value, int valueColor) {
        return Text.literal("[LegendWatch] " + setting + ": ")
                .styled(style -> style.withColor(0xFFAA00))
                .append(Text.literal(value).styled(style -> style.withColor(valueColor)));
    }

    private static void sendStatus(ClientPlayerEntity player, String setting, String value, int valueColor) {
        player.sendMessage(statusMessage(setting, value, valueColor), false);
    }
    *///?}

    //? if >=26.1 {
    private static KeyMapping registerKeyBinding(String translationKey) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(
                translationKey,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY
        ));
    }
    //?} else if >=1.21.9 {
    /*private static KeyBinding registerKeyBinding(String translationKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                translationKey,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY
        ));
    }
    *///?} else {
    /*private static KeyBinding registerKeyBinding(String translationKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                translationKey,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.category.legendwatch.general"
        ));
    }*/
    //?}
}
