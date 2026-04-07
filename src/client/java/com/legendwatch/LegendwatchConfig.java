package com.legendwatch;

import java.util.concurrent.atomic.AtomicBoolean;

public class LegendwatchConfig {

    // When false: mod does nothing at all — nametags are completely vanilla
    public static final AtomicBoolean modEnabled = new AtomicBoolean(true);

    // When false: show legendary name as plain gold text instead of the bitmap icon
    public static final AtomicBoolean iconsEnabled = new AtomicBoolean(true);

    // When false: use the solid (black border) icon sheet instead of the transparent one
    public static final AtomicBoolean transparentIconsEnabled = new AtomicBoolean(true);

    // When false: predicted legendaries (obtained by kill) are hidden entirely
    public static final AtomicBoolean predictedEnabled = new AtomicBoolean(true);
}
