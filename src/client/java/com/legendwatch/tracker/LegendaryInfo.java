package com.legendwatch.tracker;

public class LegendaryInfo {
    public final String itemName;
    public final long craftedAtTimestamp;
    // true = obtained by killing a player who had this legendary; not guaranteed to be held
    public final boolean predicted;

    public LegendaryInfo(String itemName, long craftedAtTimestamp) {
        this(itemName, craftedAtTimestamp, false);
    }

    public LegendaryInfo(String itemName, long craftedAtTimestamp, boolean predicted) {
        this.itemName = itemName;
        this.craftedAtTimestamp = craftedAtTimestamp;
        this.predicted = predicted;
    }

    public LegendaryInfo asPredicted() {
        if (predicted) return this;
        return new LegendaryInfo(itemName, craftedAtTimestamp, true);
    }

    public LegendaryInfo asConfirmed() {
        if (!predicted) return this;
        return new LegendaryInfo(itemName, craftedAtTimestamp, false);
    }
}
