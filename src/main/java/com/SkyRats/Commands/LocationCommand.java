package com.SkyRats.Commands;

import com.SkyRats.Core.Features.PlayerLocationChecker;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class LocationCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.gui.getChat().addMessage(Component.literal(PlayerLocationChecker.getLocation()));
        }
        return 1;
    }
}

