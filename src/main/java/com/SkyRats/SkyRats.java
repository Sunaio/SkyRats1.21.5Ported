package com.SkyRats;

import com.SkyRats.Core.Commands.CommandRegisterAll;
import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;
import com.SkyRats.Core.Features.Notifications.AlertMessagePopup;
import com.SkyRats.Core.Features.PlayerLocationChecker;
import com.SkyRats.Features.Alerts.ChatManager;
import com.SkyRats.Features.Mining.ShaftDetector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(SkyRats.MODID)
public class SkyRats {
    public static final String MODID = "skyrats";

    private final AlertMessagePopup alertPopup = new AlertMessagePopup();
    private final MineshaftTracker tracker = new MineshaftTracker();

    public SkyRats() {
        IEventBus bus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        // Register commands
        CommandRegisterAll.setMineshaftTracker(tracker);

        // Register client-side event listeners
        MinecraftForge.EVENT_BUS.register(new ChatManager(alertPopup));
        MinecraftForge.EVENT_BUS.register(alertPopup);
        MinecraftForge.EVENT_BUS.register(new PlayerLocationChecker());
        MinecraftForge.EVENT_BUS.register(new ShaftDetector(tracker));
    }
}
