package com.SkyRats.Features.Alerts;

import com.SkyRats.Core.Features.Notifications.AlertMessagePopup;
import com.SkyRats.Core.GUI.FeatureSettings;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChatManager {
    private static AlertMessagePopup alertMessagePopup;

    public ChatManager(AlertMessagePopup popup) {
        alertMessagePopup = popup;
    }

    @SubscribeEvent
    public static void onChatReceived(ClientChatEvent event) {
        String msg = event.getMessage();

        if (alertMessagePopup == null) return;

        if (FeatureSettings.isFeatureEnabled("Alerts", "Mining Ability Cooldown Notification")) {
            MiningAbilityAlert.check(msg, alertMessagePopup);
        }

        if (FeatureSettings.isFeatureEnabled("Alerts", "Reminder to Switch Ability Notification")) {
            SwitchAlert.check(msg, alertMessagePopup);
        }
    }
}
