package com.SkyRats.Commands;

import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;
import com.SkyRats.Core.Features.Mineshafts.ShaftTypes;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;


import java.util.*;

public class ShaftCommand {
    private static MineshaftTracker mineshaftTracker = null;

    public static void setMineshaftTracker(MineshaftTracker tracker) {
        mineshaftTracker = tracker;
    }

    public static int execute(CommandContext<CommandSourceStack> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mineshaftTracker == null) return 0;

        mc.gui.getChat().addMessage(Component.literal("Shaft Data:").withStyle(ChatFormatting.GREEN));

        List<Map.Entry<ShaftTypes, Integer>> entries = new ArrayList<>();
        for (ShaftTypes type : ShaftTypes.values()) {
            int count = mineshaftTracker.getCount(type);
            if (count > 0) {
                entries.add(new AbstractMap.SimpleEntry<>(type, count));
            }
        }

        entries.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        for (Map.Entry<ShaftTypes, Integer> entry : entries) {
            ShaftTypes type = entry.getKey();
            int count = entry.getValue();

            mc.gui.getChat().addMessage(
                    Component.literal(type.name() + ": ")
                            .withStyle(type.getColor())
                            .append(Component.literal(String.valueOf(count)).withStyle(ChatFormatting.YELLOW))
            );
        }

        mc.gui.getChat().addMessage(
                Component.literal("Total: ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(String.valueOf(mineshaftTracker.getTotalShafts())).withStyle(ChatFormatting.GREEN))
        );

        mc.gui.getChat().addMessage(
                Component.literal("Mineshafts since last ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("JASPER").withStyle(ChatFormatting.LIGHT_PURPLE))
                        .append(Component.literal(": ").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(String.valueOf(mineshaftTracker.getSinceJasper())).withStyle(ChatFormatting.RED))
        );

        return 1;
    }

}

