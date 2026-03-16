package com.legendwatch;

import java.util.concurrent.atomic.AtomicBoolean;

public class LegendwatchConfig {

    // When false: mod does nothing at all — nametags are completely vanilla
    public static final AtomicBoolean modEnabled = new AtomicBoolean(true);

    // When false: show legendary name as plain gold text instead of the bitmap icon
    public static final AtomicBoolean iconsEnabled = new AtomicBoolean(true);
}
