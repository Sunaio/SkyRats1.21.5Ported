package com.SkyRats.Features.Mining;

import com.SkyRats.Core.Features.Mineshafts.MineshaftTracker;
import com.SkyRats.Core.Features.PlayerLocationChecker;
import com.SkyRats.Core.Features.SoundGlobal;
import com.SkyRats.Core.GUI.FeatureSettings;
import com.SkyRats.Core.Features.Mineshafts.ShaftTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ShaftDetector {

    private static MineshaftTracker tracker;
    private static boolean checked = false;
    private static boolean wasInShaft = false;
    private static boolean typeSent = false;
    private static int incrementJasp = 0;
    private static int tickCooldown = 0;
    private static final int COOLDOWN_TIME = 35;
    private static final Random random = new Random();

    public ShaftDetector(MineshaftTracker tracker) {
        ShaftDetector.tracker = tracker;
    }

    // Detect blocks on player's screen with ray tracing cone
    public static void detectGemstones() {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        if (world == null || mc.player == null) return;

        Vec3 eyePos = mc.player.getEyePosition(1.0f);
        Vec3 lookVec = mc.player.getLookAngle();

        final int numRays = 35;          // More rays = wider coverage. Beware of lag.
        final double coneAngle = 1.33;   // Cone angle in radians
        final double maxDistance = 15.0; // Max detection distance

        ShaftTypes detectedType = null;

        for (int i = 0; i < numRays; i++) {
            // Random direction slightly offset from main lookVec
            Vec3 randomVec = lookVec
                    .add(
                            (random.nextDouble() - 0.5) * coneAngle,
                            (random.nextDouble() - 0.5) * coneAngle,
                            (random.nextDouble() - 0.5) * coneAngle
                    )
                    .normalize();

            Vec3 rayEnd = eyePos.add(randomVec.scale(maxDistance));
            BlockHitResult hit = world.clip(new net.minecraft.world.level.ClipContext(
                    eyePos,
                    rayEnd,
                    net.minecraft.world.level.ClipContext.Block.OUTLINE,
                    net.minecraft.world.level.ClipContext.Fluid.NONE,
                    mc.player));

            if (hit != null && hit.getType() == BlockHitResult.Type.BLOCK) {
                BlockPos pos = hit.getBlockPos();
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                ShaftTypes type = mapBlockToShaftType(block, state);
                if (type != null) {
                    detectedType = type;
                    break;
                }
            }
        }

        if (detectedType != null && !checked) {
            tracker.incrementShaft(detectedType);
            checked = true;
            // Combine color code + string then reset formatting for next
            String colorName = detectedType.getColor() + detectedType.name() + net.minecraft.ChatFormatting.RESET;

            mc.player.displayClientMessage(Component.literal(net.minecraft.ChatFormatting.RED + "[SR ShaftDetection] Detected: " + colorName),
                    false);
            typeSent = true;
        }
    }

    // Return type of Mineshaft from block type and blockstate (meta replaced with blockstate properties)
    private static ShaftTypes mapBlockToShaftType(Block block, BlockState state) {
        // You will need to adapt these meta checks for your custom blocks.
        // Vanilla blocks meta is replaced with blockstate properties in 1.13+
        // For example, stained_glass color is stored in block state "color" property

        if (block == Blocks.GOLD_BLOCK) {
            tracker.incrementSinceJasper();
            return ShaftTypes.VANGUARD;
        } else if (block == Blocks.STONE) {
            // Polished Diorite in 1.21.4 is its own block - adjust if needed
            // Here meta==4 was polished diorite in 1.8.9
            // You might need to check block directly or specific block state properties
            // Return Titanium if matches
            return ShaftTypes.TITANIUM; // Adapt logic as needed
        } else if (block == Blocks.CLAY) {
            tracker.incrementSinceJasper();
            return ShaftTypes.TUNGSTEN;
        } else if (block == Blocks.ORANGE_TERRACOTTA) {
            tracker.incrementSinceJasper();
            return ShaftTypes.UMBER;
        } else if (block == Blocks.WHITE_STAINED_GLASS
                || block == Blocks.ORANGE_STAINED_GLASS
                || block == Blocks.MAGENTA_STAINED_GLASS
                || block == Blocks.LIGHT_BLUE_STAINED_GLASS
                || block == Blocks.YELLOW_STAINED_GLASS
                || block == Blocks.LIME_STAINED_GLASS
                || block == Blocks.PURPLE_STAINED_GLASS
                || block == Blocks.BLUE_STAINED_GLASS
                || block == Blocks.BROWN_STAINED_GLASS
                || block == Blocks.GREEN_STAINED_GLASS
                || block == Blocks.RED_STAINED_GLASS
                || block == Blocks.BLACK_STAINED_GLASS) {

            // Map each glass color block to ShaftTypes
            if (block == Blocks.WHITE_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.OPAL;
            }
            if (block == Blocks.ORANGE_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.AMBER;
            }
            if (block == Blocks.MAGENTA_STAINED_GLASS) {
                tracker.resetSinceJasper();
                return ShaftTypes.JASPER;
            }
            if (block == Blocks.LIGHT_BLUE_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.SAPPHIRE;
            }
            if (block == Blocks.YELLOW_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.TOPAZ;
            }
            if (block == Blocks.LIME_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.JADE;
            }
            if (block == Blocks.PURPLE_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.AMETHYST;
            }
            if (block == Blocks.BLUE_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.AQUAMARINE;
            }
            if (block == Blocks.BROWN_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.CITRINE;
            }
            if (block == Blocks.GREEN_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.PERIDOT;
            }
            if (block == Blocks.RED_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.RUBY;
            }
            if (block == Blocks.BLACK_STAINED_GLASS) {
                tracker.incrementSinceJasper();
                return ShaftTypes.ONYX;
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        if (mc.player == null || world == null) return;

        if (tickCooldown > 0) {
            tickCooldown--;
            return;
        }

        tickCooldown = COOLDOWN_TIME;

        if (!FeatureSettings.isFeatureEnabled("Mining", "Mineshaft Tracker")) return;

        String location = PlayerLocationChecker.getLocation();
        if (location.equalsIgnoreCase("N/A")) return;

        boolean isInShaft = location.equalsIgnoreCase("Glacite Mineshafts");

        if (isInShaft && !wasInShaft) {
            checked = false;
        }

        if (isInShaft && !checked) {
            detectGemstones();

            // Play XP orb pickup sound locally
            SoundGlobal.playGlobalSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        }

        wasInShaft = isInShaft;

        if ((location.equalsIgnoreCase("Glacite Tunnels") || location.equalsIgnoreCase("Dwarven Base Camp")) && typeSent) {
            typeSent = false;

            if (tracker.getSinceJasper() == (incrementJasp + 20)) {
                incrementJasp += 20;
                SoundGlobal.playGlobalSound(SoundEvents.ANVIL_LAND, 1.0F, 1.0F);
                mc.gui.getChat().addMessage(Component.literal("You didn't get a "
                        + net.minecraft.ChatFormatting.LIGHT_PURPLE + "Jasper" + " in " + tracker.getSinceJasper() + " mineshafts LOL! 🫡"));
            }
        }
    }
}
