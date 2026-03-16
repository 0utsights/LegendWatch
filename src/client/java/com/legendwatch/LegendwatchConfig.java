package com.legendwatch;

import java.util.concurrent.atomic.AtomicBoolean;

public class LegendwatchConfig {

    // AtomicBoolean avoids non-atomic read-modify-write on the toggle
    public static final AtomicBoolean iconsEnabled = new AtomicBoolean(true);
}
