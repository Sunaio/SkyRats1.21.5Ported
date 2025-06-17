package com.SkyRats.Core.Commands;

import com.SkyRats.Commands.LocationCommand;
import com.SkyRats.Commands.MenuCommand;
import com.SkyRats.Commands.ShaftCommand;
import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "skyrats")
public class CommandRegisterAll {
    private static MineshaftTracker mineshaftTracker;

    public static void setMineshaftTracker(MineshaftTracker tracker) {
        CommandRegisterAll.mineshaftTracker = tracker;
        ShaftCommand.setMineshaftTracker(tracker);
    }

    @SubscribeEvent
    public static void onClientCommandRegister(RegisterClientCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("sr")
                        .executes(MenuCommand::execute)
                        .then(Commands.literal("shaft").executes(ShaftCommand::execute))
                        .then(Commands.literal("wmi").executes(LocationCommand::execute))
        );
    }
}

