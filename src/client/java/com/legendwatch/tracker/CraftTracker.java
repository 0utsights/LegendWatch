package com.legendwatch.tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CraftTracker {

    // Maps username -> ordered list of legendaries crafted this match
    public static final ConcurrentHashMap<String, List<LegendaryInfo>> CRAFT_MAP = new ConcurrentHashMap<>();

    public static void init() {
        // Called on client init, nothing to set up yet
    }

    public static void onMatchReset() {
        CRAFT_MAP.clear();
    }

    public static void recordCraft(String username, String itemName) {
        CRAFT_MAP.computeIfAbsent(username, k -> Collections.synchronizedList(new ArrayList<>()))
                 .add(new LegendaryInfo(itemName, System.currentTimeMillis()));
    }

    public static boolean hasCrafted(String username) {
        List<LegendaryInfo> list = CRAFT_MAP.get(username);
        return list != null && !list.isEmpty();
    }

    // Returns an unmodifiable snapshot of the list, or empty if none
    public static List<LegendaryInfo> getCrafts(String username) {
        List<LegendaryInfo> list = CRAFT_MAP.get(username);
        if (list == null) return List.of();
        synchronized (list) {
            return List.copyOf(list);
        }
    }

    // Kept for any callers that only need the first craft
    public static LegendaryInfo getCraft(String username) {
        List<LegendaryInfo> list = CRAFT_MAP.get(username);
        if (list == null || list.isEmpty()) return null;
        return list.get(0);
    }
}
