package com.legendwatch.tracker;

public class LegendaryInfo {
    public final String itemName;
    public final long craftedAtTimestamp;

    public LegendaryInfo(String itemName, long craftedAtTimestamp) {
        this.itemName = itemName;
        this.craftedAtTimestamp = craftedAtTimestamp;
    }
}