package com.SkyRats.Core.Features.Mineshafts;

import java.util.EnumMap;
import java.util.Map;

// Keeps and store all mineshaft data
public class MineshaftTracker {
    private final Map<ShaftTypes, Integer> shaftCounts = new EnumMap<ShaftTypes, Integer>(ShaftTypes.class);
    private int shaftSinceJasper;

    public MineshaftTracker() {
        shaftSinceJasper = 0;
        for (ShaftTypes type : ShaftTypes.values()) {
            shaftCounts.put(type, 0);
        }
    }

    // Increase shaft count
    public void incrementShaft(ShaftTypes type) {
        shaftCounts.put(type, shaftCounts.get(type) + 1);
    }

    // Increase shaft since last time player got a Jasper shaft
    public void incrementSinceJasper() {
        shaftSinceJasper++;
    }

    // Getter
    public int getSinceJasper() {
        return shaftSinceJasper;
    }

    // Reset shafts since last Jasper back to 0
    public void resetSinceJasper() {
        shaftSinceJasper = 0;
    }

    // Getter
    public int getCount(ShaftTypes type) {
        return shaftCounts.get(type);
    }

    // Calculate and return all shafts stored
    public int getTotalShafts() {
        int totalShafts = 0;
        for (Integer count : shaftCounts.values()) {
            totalShafts += count;
        }
        return totalShafts;
    }
}
