package com.legendwatch.tracker;

import java.util.concurrent.ConcurrentHashMap;

public class CraftTracker {

    public static final ConcurrentHashMap<String, LegendaryInfo> CRAFT_MAP = new ConcurrentHashMap<>();

    public static void init() {
        // Called on client init, nothing to set up yet
    }

    public static void onMatchReset() {
        CRAFT_MAP.clear();
    }

    public static void recordCraft(String username, String itemName) {
        CRAFT_MAP.put(username, new LegendaryInfo(itemName, System.currentTimeMillis()));
    }

    public static boolean hasCrafted(String username) {
        return CRAFT_MAP.containsKey(username);
    }

    public static LegendaryInfo getCraft(String username) {
        return CRAFT_MAP.get(username);
    }
}