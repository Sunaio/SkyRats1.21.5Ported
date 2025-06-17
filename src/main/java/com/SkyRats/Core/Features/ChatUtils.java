package com.SkyRats.Core.Features;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

// Chat ulitities for clickable messages
public class ChatUtils {
    public static void sendClickableMessage(String text, ChatFormatting textColor,
                                            String clickableText, ChatFormatting clickableTextColor,
                                            String command, String hoverText) {
        Minecraft mc = Minecraft.getInstance();

        // [SR] prefix, always bold
        Component srPrefix = Component.literal("[SR] ")
                .setStyle(Style.EMPTY.withColor(textColor).withBold(true));

        // Normal message text, not bold
        Component message = Component.literal(text)
                .setStyle(Style.EMPTY.withColor(textColor).withBold(false));

        // Clickable text, bold + color, with click and hover events
        Component clickable = Component.literal(clickableText)
                .setStyle(Style.EMPTY
                        .withColor(clickableTextColor)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal(hoverText).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)))));

        // Combine prefix + message + clickable
        Component fullMessage = srPrefix.copy().append(message).append(clickable);

        // Send message to player chat, second argument 'false' means not system message
        if (mc.player != null) {
            mc.gui.getChat().addMessage(fullMessage);
        }
    }
}
