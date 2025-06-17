package com.SkyRats.Features.Alerts;

import com.SkyRats.Core.Features.ChatUtils;
import com.SkyRats.Core.Features.Notifications.AlertMessagePopup;
import com.SkyRats.Core.Features.PlayerLocationChecker;
import com.SkyRats.Core.Features.SoundGlobal;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvents;

import java.util.Set;

public class SwitchAlert {
    private static boolean abilitySwitched = false;
    private static boolean enteredShaft = false;

    private static final Set<String> SWITCH_AWAY_MESSAGES = Set.of(
            "You selected Mining Speed Boost as your Pickaxe Ability. This ability will apply to all of your pickaxes!",
            "You selected Anomalous Desire as your Pickaxe Ability. This ability will apply to all of your pickaxes!",
            "You selected Maniac Miner as your Pickaxe Ability. This ability will apply to all of your pickaxes!",
            "You selected Gemstone Infusion as your Pickaxe Ability. This ability will apply to all of your pickaxes!",
            "You selected Sheer Force as your Pickaxe Ability. This ability will apply to all of your pickaxes!"
    );

    public static void check(String msg, AlertMessagePopup alert) {
        String playerLocation = PlayerLocationChecker.getLocation();

        if (SWITCH_AWAY_MESSAGES.contains(msg)) {
            abilitySwitched = true;
        } else if (msg.equalsIgnoreCase("You selected Pickobulus as your Pickaxe Ability. This ability will apply to all of your pickaxes!")) {
            abilitySwitched = false;
        } else {
            return;
        }

        if (playerLocation.equalsIgnoreCase("Glacite Mineshafts")) {
            enteredShaft = true;
        }

        if ((playerLocation.equalsIgnoreCase("Glacite Tunnels") || playerLocation.equalsIgnoreCase("Dwarven Base Camp"))
                && enteredShaft && abilitySwitched) {

            alert.display("SWITCH", 2000);
            ChatUtils.sendClickableMessage(
                    "Reminder to switch pickaxe ability!",
                    ChatFormatting.AQUA,
                    "OPEN HOTM",
                    ChatFormatting.DARK_GREEN,
                    "/hotm",
                    "Runs /hotm"
            );

            SoundGlobal.playGlobalSound(SoundEvents.ANVIL_BREAK, 1.0F, 1.0F);

            enteredShaft = false;
        }
    }
}
