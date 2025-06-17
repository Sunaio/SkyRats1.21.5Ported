package com.SkyRats.Core.Features.Notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "skyrats", value = Dist.CLIENT)
public class AlertMessagePopup {
    private static String msg;
    private static long displayUntil;

    public static void display(String message, long displayTimeMs) {
        msg = message;
        displayUntil = System.currentTimeMillis() + displayTimeMs;
    }

    @SubscribeEvent
    public static void onRenderOverlay(CustomizeGuiOverlayEvent event) {
        if (System.currentTimeMillis() >= displayUntil || msg == null || msg.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Font font = mc.font;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        float scale = 5.0f;
        int stringWidth = font.width(msg);
        int stringHeight = font.lineHeight;

        float x = (screenWidth / 2.0f - stringWidth * scale / 2.0f) / scale;
        float y = (screenHeight / 2.0f - stringHeight * scale / 2.0f) / scale;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawString(font, msg, (int) x, (int) y, 0xFFFFC0CB, false);
        guiGraphics.pose().popPose();
    }
}
