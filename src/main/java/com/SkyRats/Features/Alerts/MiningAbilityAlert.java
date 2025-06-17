package com.SkyRats.Features.Alerts;

import com.SkyRats.Core.Features.Notifications.AlertMessagePopup;
import com.SkyRats.Core.Features.SoundGlobal;
import net.minecraft.sounds.SoundEvents;

public class MiningAbilityAlert {
    public static void check(String msg, AlertMessagePopup alert) {
        if(msg.equalsIgnoreCase("Pickobulus is now available!")
                || msg.equalsIgnoreCase("Mining Speed Boost is now available!")
                || msg.equalsIgnoreCase("Anomalous Desire Pickaxe Ability!")
                || msg.equalsIgnoreCase("Maniac Miner is now available!")
                || msg.equalsIgnoreCase("Gemstone Infusion is now available!")
                || msg.equalsIgnoreCase("Sheer Force is now available!")) {

            alert.display("ABILITY", 2000);

            SoundGlobal.playGlobalSound(SoundEvents.ENDER_DRAGON_GROWL, 1.0F, 1.0F);
        }
    }
}
