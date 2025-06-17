package com.SkyRats.GUI;

import com.SkyRats.Core.Features.ColorAnimations;
import com.SkyRats.Core.GUI.FeatureSettings;
import com.SkyRats.Core.GUI.Settings;
import com.SkyRats.Core.GUI.SettingsManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.io.File;
import java.util.List;

public class HomeGUI extends Screen {

    private static final int LEFT_PANEL_WIDTH = 150;
    private static final int RIGHT_PANEL_WIDTH = 430;
    private static final int PANEL_WIDTH = LEFT_PANEL_WIDTH + RIGHT_PANEL_WIDTH;
    private static final int PANEL_HEIGHT = 320;
    private static final int SIDEBAR_PADDING_X = 10;
    private static final int SIDEBAR_PADDING_Y = 40;
    private static final int FEATURE_SPACING_Y = 22;
    private static final int FEATURE_HEIGHT = 12;
    private static final int TOGGLE_LABEL_OFFSET_Y = 4;

    private final File featureFile = new File("SkyRats/settings.json");
    private final String[] features = {"Home", "Alerts", "Mining", "Rift", "HUD", "Others"};
    private int selectedFeature = 0;

    // Cached panel positions (set on init)
    private int panelX, panelY;

    public HomeGUI() {
        super(Component.literal("SkyRats GUI"));
        FeatureSettings.run();
    }

    @Override
    protected void init() {
        panelX = (width - PANEL_WIDTH) / 2;
        panelY = (height - PANEL_HEIGHT) / 2;
        // Initialize widgets here if needed in future
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics, mouseX, mouseY, partialTicks);

        int chroma = ColorAnimations.getChromaColor(8f, 0f);
        PoseStack pose = graphics.pose();

        // Draw left panel background
        graphics.fill(panelX, panelY, panelX + LEFT_PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xCC111111);
        // Draw right panel background
        graphics.fill(panelX + LEFT_PANEL_WIDTH, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xCC111111);

        // Title
        graphics.drawString(font,
                Component.literal("SkyRats")
                        .withStyle(Style.EMPTY.withBold(true).withUnderlined(true)),
                panelX + 10, panelY + 10, chroma);

        // Creator smaller text scaled down
        pose.pushPose();
        pose.scale(0.87f, 0.87f, 1f);
        graphics.drawString(font,
                Component.literal("By Sunaio & A_Blender_")
                        .withStyle(Style.EMPTY.withBold(true)),
                (int) ((panelX + 10) / 0.87f),
                (int) ((panelY + 10 + 14) / 0.87f),
                chroma);
        pose.popPose();

        // Vertical separator line
        graphics.fill(panelX + LEFT_PANEL_WIDTH, panelY, panelX + LEFT_PANEL_WIDTH + 1, panelY + PANEL_HEIGHT, chroma);

        renderSidebarFeatures(graphics, mouseX, mouseY, chroma);

        renderRightPanelContent(graphics, panelX + LEFT_PANEL_WIDTH + SIDEBAR_PADDING_X, panelY + 20, mouseX, mouseY);
    }

    private void renderSidebarFeatures(GuiGraphics graphics, int mouseX, int mouseY, int chroma) {
        int startY = panelY + SIDEBAR_PADDING_Y;

        for (int i = 0; i < features.length; i++) {
            int featureY = startY + i * FEATURE_SPACING_Y;
            // Check if the mouse is hovering over the feature in the sidebar
            boolean hovered = mouseX >= panelX + SIDEBAR_PADDING_X && mouseX <= panelX + LEFT_PANEL_WIDTH - SIDEBAR_PADDING_X &&
                    mouseY >= featureY && mouseY <= featureY + FEATURE_HEIGHT;

            // Color cyan-ish if selected or hovered, white otherwise
            int color = (i == selectedFeature || hovered) ? 0xFF55FFFF : 0xFFFFFFFF;

            // Underline selected feature
            Component textComponent = Component.literal(features[i]);
            if (i == selectedFeature) {
                textComponent = textComponent.copy().setStyle(Style.EMPTY.withUnderlined(true));
            }

            graphics.drawString(font, textComponent, panelX + SIDEBAR_PADDING_X, featureY, color);
        }
    }

    private void renderRightPanelContent(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        int chroma = ColorAnimations.getChromaColor(8f, 0f);

        // Draw the right panel's title with bold and underline
        graphics.drawString(font,
                Component.literal(features[selectedFeature])
                        .withStyle(Style.EMPTY.withBold(true).withUnderlined(true)),
                x, y, chroma);

        List<Settings> toggles = FeatureSettings.getFeatureSettings().get(features[selectedFeature]);
        if (toggles == null || toggles.isEmpty()) {
            // If no toggles available, show "WIP" text in gray
            graphics.drawString(font, Component.literal("WIP"), x, y + 20, 0xFFAAAAAA);
            return;
        }

        int currentY = y + 20;
        PoseStack pose = graphics.pose();

        for (Settings toggle : toggles) {
            toggle.setX(x);
            toggle.setY(currentY);

            // Blue text if enabled, gray if disabled
            int labelColor = toggle.getValue() ? 0xFF3F76E4 : 0xFF555555;
            graphics.drawString(font, Component.literal(toggle.getLabel()), x, currentY + TOGGLE_LABEL_OFFSET_Y, labelColor);
            toggle.draw(graphics, mouseX, mouseY);

            String desc = toggle.getDescription();
            currentY += toggle.getHeight();

            if (desc != null && !desc.isEmpty()) {
                pose.pushPose();
                pose.scale(0.77F, 0.77F, 1.0F);
                // White description if enabled, gray if disabled
                int descColor = toggle.getValue() ? 0xFFFFFFFF : 0xFF555555;
                graphics.drawString(font, Component.literal(desc),
                        (int) ((x + 4) / 0.77F),
                        (int) ((currentY + 2) / 0.77F),
                        descColor);
                pose.popPose();
                currentY += 14;
            } else {
                currentY += 5;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        int startY = panelY + SIDEBAR_PADDING_Y;

        // Check sidebar clicks
        for (int i = 0; i < features.length; i++) {
            int yPos = startY + i * FEATURE_SPACING_Y;
            if (mouseX >= panelX + SIDEBAR_PADDING_X && mouseX <= panelX + LEFT_PANEL_WIDTH - SIDEBAR_PADDING_X &&
                    mouseY >= yPos && mouseY <= yPos + FEATURE_HEIGHT) {
                selectedFeature = i;
                return true;
            }
        }

        // Check toggles clicks
        List<Settings> toggles = FeatureSettings.getFeatureSettings().get(features[selectedFeature]);
        if (toggles != null) {
            for (Settings toggle : toggles) {
                if (toggle.isHovered((int) mouseX, (int) mouseY)) {
                    toggle.toggle();
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void removed() {
        // Save settings when the screen is closed
        SettingsManager.save(FeatureSettings.getFeatureSettings(), featureFile);
        super.removed();
    }
}
