package com.legendwatch;

import com.legendwatch.tracker.CraftTracker;
import com.legendwatch.listener.ChatListener;
import net.fabricmc.api.ClientModInitializer;

public class LegendwatchClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CraftTracker.init();
		ChatListener.init();
	}
}