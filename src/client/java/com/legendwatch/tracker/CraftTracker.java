package com.legendwatch.tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CraftTracker {

    public static final String GERALD_THE_SNIFFER = "Gerald the Sniffer";

    // Maps username -> ordered list of legendaries crafted this match
    public static final ConcurrentHashMap<String, List<LegendaryInfo>> CRAFT_MAP = new ConcurrentHashMap<>();

    public static void init() {
        // Called on client init, nothing to set up yet
    }

    public static void onMatchReset() {
        CRAFT_MAP.clear();
    }

    public static void recordCraft(String username, String itemName) {
        upsertLegendary(username, itemName, false, System.currentTimeMillis());
    }

    public static void recordPredicted(String username, String itemName) {
        upsertLegendary(username, itemName, true, System.currentTimeMillis());
    }

    // Called when an elimination message is seen.
    // Transfers all of slain's legendaries (confirmed + predicted) into slayer's list as predicted,
    // then removes slain from the map entirely.
    public static void onPlayerEliminated(String slain, String slayer) {
        List<LegendaryInfo> slainCrafts = CRAFT_MAP.remove(slain);
        if (slainCrafts == null || slainCrafts.isEmpty()) return;
        if (slayer == null || slayer.isBlank()) return;

        List<LegendaryInfo> slayerList = CRAFT_MAP.computeIfAbsent(
                slayer, k -> Collections.synchronizedList(new ArrayList<>()));

        synchronized (slainCrafts) {
            synchronized (slayerList) {
                for (LegendaryInfo info : slainCrafts) {
                    upsertLegendary(slayerList, info.itemName, true, info.craftedAtTimestamp);
                }
            }
        }
    }

    public static void onLegendaryKillObserved(String username, String itemName,
                                               boolean experimentalGeraldTrackingEnabled) {
        if (username == null || username.isBlank() || itemName == null || itemName.isBlank()) return;

        if (experimentalGeraldTrackingEnabled
                && hasConfirmedLegendary(username, GERALD_THE_SNIFFER)
                && !hasConfirmedLegendary(username, itemName)) {
            if (canConfirmGeraldAs(itemName)) {
                replaceGeraldWithConfirmedLegendary(username, itemName);
            } else {
                recordPredicted(username, itemName);
            }
            return;
        }

        if (confirmLegendary(username, itemName)) return;
        if (hasLegendary(username, itemName)) return;

        recordCraft(username, itemName);
    }

    public static boolean hasCrafted(String username) {
        List<LegendaryInfo> list = CRAFT_MAP.get(username);
        return list != null && !list.isEmpty();
    }

    public static boolean hasLegendary(String username, String itemName) {
        return getLegendary(username, itemName) != null;
    }

    public static boolean hasConfirmedLegendary(String username, String itemName) {
        LegendaryInfo info = getLegendary(username, itemName);
        return info != null && !info.predicted;
    }

    public static boolean confirmLegendary(String username, String itemName) {
        List<LegendaryInfo> list = CRAFT_MAP.get(username);
        if (list == null) return false;

        synchronized (list) {
            int index = findLegendaryIndex(list, itemName);
            if (index < 0) return false;

            LegendaryInfo info = list.get(index);
            if (!info.predicted) return false;

            list.set(index, info.asConfirmed());
            return true;
        }
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

    public static LegendaryInfo getLegendary(String username, String itemName) {
        List<LegendaryInfo> list = CRAFT_MAP.get(username);
        if (list == null) return null;

        synchronized (list) {
            int index = findLegendaryIndex(list, itemName);
            return index >= 0 ? list.get(index) : null;
        }
    }

    private static void upsertLegendary(String username, String itemName, boolean predicted,
                                        long timestamp) {
        List<LegendaryInfo> list = CRAFT_MAP.computeIfAbsent(
                username, k -> Collections.synchronizedList(new ArrayList<>())
        );

        synchronized (list) {
            upsertLegendary(list, itemName, predicted, timestamp);
        }
    }

    private static void upsertLegendary(List<LegendaryInfo> list, String itemName, boolean predicted,
                                        long timestamp) {
        int index = findLegendaryIndex(list, itemName);
        if (index < 0) {
            list.add(new LegendaryInfo(itemName, timestamp, predicted));
            return;
        }

        LegendaryInfo existing = list.get(index);
        if (!predicted && existing.predicted) {
            list.set(index, existing.asConfirmed());
        }
    }

    private static int findLegendaryIndex(List<LegendaryInfo> list, String itemName) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).itemName.equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean canConfirmGeraldAs(String itemName) {
        if (isLegendaryConfirmedAnywhere(itemName)) return true;
        return !isLegendaryTrackedAnywhere(itemName);
    }

    private static boolean isLegendaryConfirmedAnywhere(String itemName) {
        for (List<LegendaryInfo> list : CRAFT_MAP.values()) {
            synchronized (list) {
                for (LegendaryInfo info : list) {
                    if (info.itemName.equals(itemName) && !info.predicted) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isLegendaryTrackedAnywhere(String itemName) {
        for (List<LegendaryInfo> list : CRAFT_MAP.values()) {
            synchronized (list) {
                for (LegendaryInfo info : list) {
                    if (info.itemName.equals(itemName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void replaceGeraldWithConfirmedLegendary(String username, String itemName) {
        List<LegendaryInfo> list = CRAFT_MAP.get(username);
        if (list == null) {
            recordCraft(username, itemName);
            return;
        }

        synchronized (list) {
            int geraldIndex = findLegendaryIndex(list, GERALD_THE_SNIFFER);
            if (geraldIndex < 0) {
                upsertLegendary(list, itemName, false, System.currentTimeMillis());
                return;
            }

            List<LegendaryInfo> updated = new ArrayList<>(list.size());
            boolean insertedReplacement = false;

            for (LegendaryInfo info : list) {
                if (info.itemName.equals(GERALD_THE_SNIFFER)) {
                    if (!insertedReplacement) {
                        updated.add(new LegendaryInfo(itemName, info.craftedAtTimestamp, false));
                        insertedReplacement = true;
                    }
                    continue;
                }

                if (info.itemName.equals(itemName)) {
                    if (!insertedReplacement) {
                        updated.add(info.asConfirmed());
                        insertedReplacement = true;
                    }
                    continue;
                }

                updated.add(info);
            }

            if (!insertedReplacement) {
                updated.add(new LegendaryInfo(itemName, System.currentTimeMillis(), false));
            }

            list.clear();
            list.addAll(updated);
        }
    }
}
