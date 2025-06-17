package com.SkyRats.Core.Features;

import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.ChatFormatting;

import java.util.Collection;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerLocationChecker {

    private static String location = "N/A";

    private static int tickCooldown = 0;
    private static final int COOLDOWN_TIME = 20;

    // Remove non-BMP chars (emojis)
    public static String removeSurrogatePairs(String input) {
        if (input == null) return null;

        StringBuilder sb = new StringBuilder();
        int length = input.length();

        for (int offset = 0; offset < length; ) {
            int codePoint = input.codePointAt(offset);

            if (codePoint <= 0xFFFF) {
                sb.append((char) codePoint);
            }

            offset += Character.charCount(codePoint);
        }

        return sb.toString();
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        // Cooldown
        if (tickCooldown > 0) {
            tickCooldown--;
            return;
        }
        tickCooldown = COOLDOWN_TIME;

        // Get scoreboard
        Scoreboard scoreboard = mc.level.getScoreboard();
        if (scoreboard == null) return;

        // Get sidebar
        Objective sidebar = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
        if (sidebar == null) return;

        // Get each sidebar entry
        Collection<PlayerScoreEntry> scores = scoreboard.listPlayerScores(sidebar);
        if (scores == null) return;

        for (PlayerScoreEntry score : scores) {
            String entry = score.owner();
            if (entry == null || entry.startsWith("#")) continue; // Skip empty sidebar entries

            Team team = scoreboard.getPlayerTeam(entry);
            String formattedEntry;

            if (team != null) {
                formattedEntry = team.getColor().toString() + entry;
            } else {
                formattedEntry = entry;
            }

            // Check for the island symbol ⏣ or another identifier Hypixel uses
            if (formattedEntry.contains("\u23E3")) {
                String stripped = ChatFormatting.stripFormatting(formattedEntry);
                location = removeSurrogatePairs(stripped).replace("\u23E3", "").trim();
                break;
            }
        }
    }

    public static String getLocation() {
        return location;
    }
}
