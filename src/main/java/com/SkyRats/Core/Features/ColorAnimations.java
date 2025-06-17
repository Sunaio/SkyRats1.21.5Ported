package com.SkyRats.Core.Features;

// Changing color code
// Right now only used for GUI line chroma color, will expand when dye animations comes
public class ColorAnimations {
    public static int getChromaColor(float speed, float offset) {
        // speed: how fast the color changes - smaller -> faster, higher -> slower
        // offset: phase offset so multiple things can have different colors

        float hue = ((System.currentTimeMillis() + (long)(offset * 1000)) % (int)(speed * 1000)) / (speed * 1000);
        // saturation and brightness
        int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);

        // Convert RGB int to ARGB int with full alpha
        return 0xFF000000 | (rgb & 0xFFFFFF);
    }
}
