package com.legendwatch;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LegendwatchConfig {

    public enum MidasDisplayMode {
        SHARPNESS_ESTIMATE("sharpness_estimate", "Sharpness Estimate"),
        KILL_COUNT("kill_count", "Kill Count"),
        OFF("off", "Off");

        private final String configValue;
        private final String displayName;

        MidasDisplayMode(String configValue, String displayName) {
            this.configValue = configValue;
            this.displayName = displayName;
        }

        public String configValue() {
            return configValue;
        }

        public String displayName() {
            return displayName;
        }

        public MidasDisplayMode next() {
            return switch (this) {
                case SHARPNESS_ESTIMATE -> KILL_COUNT;
                case KILL_COUNT -> OFF;
                case OFF -> SHARPNESS_ESTIMATE;
            };
        }

        public static MidasDisplayMode fromConfigValue(String value, MidasDisplayMode defaultValue) {
            if (value == null) return defaultValue;

            for (MidasDisplayMode mode : values()) {
                if (mode.configValue.equalsIgnoreCase(value)) {
                    return mode;
                }
            }

            return defaultValue;
        }
    }

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("legendwatch.properties");

    // When false: mod does nothing at all - nametags are completely vanilla
    public static final AtomicBoolean modEnabled = new AtomicBoolean(true);

    // When false: show legendary name as plain gold text instead of the bitmap icon
    public static final AtomicBoolean iconsEnabled = new AtomicBoolean(true);

    // When false: use the solid (black border) icon sheet instead of the transparent one
    public static final AtomicBoolean transparentIconsEnabled = new AtomicBoolean(true);

    // When false: predicted legendaries (obtained by kill) are hidden entirely
    public static final AtomicBoolean predictedEnabled = new AtomicBoolean(true);

    // Experimental: if a player with confirmed Gerald gets a kill with another legendary,
    // predict that copied legendary until it is confirmed through later observation.
    public static final AtomicBoolean experimentalGeraldTrackingEnabled = new AtomicBoolean(true);

    // Experimental: controls how Midas Sword progress is displayed.
    public static final AtomicReference<MidasDisplayMode> midasDisplayMode =
            new AtomicReference<>(MidasDisplayMode.SHARPNESS_ESTIMATE);

    public static void load() {
        Properties properties = new Properties();

        if (Files.exists(CONFIG_PATH)) {
            try (InputStream inputStream = Files.newInputStream(CONFIG_PATH)) {
                properties.load(inputStream);
            } catch (IOException e) {
                System.err.println("[LegendWatch] Failed to load config: " + e.getMessage());
            }
        }

        modEnabled.set(readBoolean(properties, "mod_enabled", modEnabled.get()));
        iconsEnabled.set(readBoolean(properties, "icons_enabled", iconsEnabled.get()));
        transparentIconsEnabled.set(readBoolean(properties, "transparent_icons_enabled",
                transparentIconsEnabled.get()));
        predictedEnabled.set(readBoolean(properties, "predicted_enabled", predictedEnabled.get()));
        experimentalGeraldTrackingEnabled.set(readBoolean(properties,
                "experimental_gerald_tracking_enabled",
                experimentalGeraldTrackingEnabled.get()));
        midasDisplayMode.set(readMidasDisplayMode(properties));

        save();
    }

    public static void save() {
        Properties properties = new Properties();
        properties.setProperty("mod_enabled", Boolean.toString(modEnabled.get()));
        properties.setProperty("icons_enabled", Boolean.toString(iconsEnabled.get()));
        properties.setProperty("transparent_icons_enabled",
                Boolean.toString(transparentIconsEnabled.get()));
        properties.setProperty("predicted_enabled", Boolean.toString(predictedEnabled.get()));
        properties.setProperty("experimental_gerald_tracking_enabled",
                Boolean.toString(experimentalGeraldTrackingEnabled.get()));
        properties.setProperty("experimental_midas_display_mode",
                midasDisplayMode.get().configValue());

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (OutputStream outputStream = Files.newOutputStream(CONFIG_PATH)) {
                properties.store(outputStream, "LegendWatch configuration");
            }
        } catch (IOException e) {
            System.err.println("[LegendWatch] Failed to save config: " + e.getMessage());
        }
    }

    private static boolean readBoolean(Properties properties, String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, Boolean.toString(defaultValue)));
    }

    private static MidasDisplayMode readMidasDisplayMode(Properties properties) {
        String explicitMode = properties.getProperty("experimental_midas_display_mode");
        if (explicitMode != null) {
            return MidasDisplayMode.fromConfigValue(explicitMode, midasDisplayMode.get());
        }

        boolean legacyEnabled = readBoolean(properties, "experimental_midas_tracking_enabled", true);
        return legacyEnabled ? MidasDisplayMode.SHARPNESS_ESTIMATE : MidasDisplayMode.OFF;
    }
}
