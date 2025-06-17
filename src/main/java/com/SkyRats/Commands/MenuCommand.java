package com.SkyRats.Commands;

import com.SkyRats.GUI.HomeGUI;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;

public class MenuCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.setScreen(new HomeGUI());
        }
        return 1;
    }
}

