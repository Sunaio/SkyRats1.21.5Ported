package com.SkyRats.Core.GUI;

import com.google.gson.annotations.Expose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

// Renders the feature switch alongside data that is saved to JSON
public class Settings implements Config {
    private int x, y;
    private final int width = 50;
    private final int height = 14;
    private final int toggleWidth = 24;
    private final int toggleHeight = height - 5;
    private int toggleX;
    private int toggleY;

    @Expose private String label;
    @Expose private boolean value;
    private String description;

    public Settings(String label, String description, boolean defaultValue) {
        this.label = label;
        this.description = description;
        this.value = defaultValue;
    }

    // Getters and setters
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean getValue() {
        return value;
    }

    @Override
    public int getNum() {
        return 0; // Unused, so return default 0
    }

    @Override
    public void setValue(boolean value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void toggle() {
        this.value = !this.value;
    }

    // GUI positioning setters and getters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
        this.toggleX = this.x + 350; // Position toggle relative to label
    }
    public void setY(int y) {
        this.y = y;
        this.toggleY = this.y + (height - toggleHeight) / 2; // center vertically
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    // Rendering the toggle button
    public void draw(GuiGraphics graphics, int mouseX, int mouseY) {
        // Draw the toggle background with border
        fillRect(graphics, toggleX, toggleY, toggleWidth, toggleHeight, 0xFF2A2A2A); // base
        fillRect(graphics, toggleX, toggleY, toggleWidth, 1, 0xFF444444);           // top border
        fillRect(graphics, toggleX, toggleY + toggleHeight - 1, toggleWidth, 1, 0xFF444444); // bottom border
        fillRect(graphics, toggleX, toggleY, 1, toggleHeight, 0xFF444444);          // left border
        fillRect(graphics, toggleX + toggleWidth - 1, toggleY, 1, toggleHeight, 0xFF444444); // right border

        // Draw the toggle thumb
        int thumbWidth = toggleWidth / 2;
        int thumbX = value ? (toggleX + thumbWidth) : toggleX;
        int thumbColor = value ? 0xFF3F76E4 : 0xFFD3D3D3; // Blue if on, gray if off
        fillRect(graphics, thumbX, toggleY, thumbWidth, toggleHeight, thumbColor);
    }

    // Check if mouse is hovering over the toggle button
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= toggleX && mouseX <= toggleX + toggleWidth &&
                mouseY >= toggleY && mouseY <= toggleY + toggleHeight;
    }

    // Helper to draw filled rectangle (x, y, width, height)
    private void fillRect(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + height, color);
    }
}
