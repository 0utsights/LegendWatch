package com.legendwatch.tracker;

public class LegendaryInfo {
    public final String itemName;
    public final long craftedAtTimestamp;
    // true = obtained by killing a player who had this legendary; not guaranteed to be held
    public final boolean predicted;
    // Experimental: preserved per-item score/counter, currently used for Midas Sword.
    public final int killCount;

    public LegendaryInfo(String itemName, long craftedAtTimestamp) {
        this(itemName, craftedAtTimestamp, false, 0);
    }

    public LegendaryInfo(String itemName, long craftedAtTimestamp, boolean predicted) {
        this(itemName, craftedAtTimestamp, predicted, 0);
    }

    public LegendaryInfo(String itemName, long craftedAtTimestamp, boolean predicted, int killCount) {
        this.itemName = itemName;
        this.craftedAtTimestamp = craftedAtTimestamp;
        this.predicted = predicted;
        this.killCount = killCount;
    }

    public LegendaryInfo asPredicted() {
        if (predicted) return this;
        return new LegendaryInfo(itemName, craftedAtTimestamp, true, killCount);
    }

    public LegendaryInfo asConfirmed() {
        if (!predicted) return this;
        return new LegendaryInfo(itemName, craftedAtTimestamp, false, killCount);
    }

    public LegendaryInfo withKillCount(int updatedKillCount) {
        if (killCount == updatedKillCount) return this;
        return new LegendaryInfo(itemName, craftedAtTimestamp, predicted, updatedKillCount);
    }

    public LegendaryInfo incrementKillCount() {
        return withKillCount(killCount + 1);
    }
}
