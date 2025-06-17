package com.SkyRats.Core.Features.Mineshafts;

import net.minecraft.ChatFormatting;

public enum ShaftTypes {
    JASPER(ChatFormatting.LIGHT_PURPLE),
    JADE(ChatFormatting.GREEN),
    AMETHYST(ChatFormatting.DARK_PURPLE),
    AMBER(ChatFormatting.GOLD),
    SAPPHIRE(ChatFormatting.AQUA),
    TOPAZ(ChatFormatting.YELLOW),
    RUBY(ChatFormatting.RED),
    OPAL(ChatFormatting.WHITE),
    AQUAMARINE(ChatFormatting.DARK_AQUA),
    PERIDOT(ChatFormatting.DARK_GREEN),
    ONYX(ChatFormatting.BLACK),
    CITRINE(ChatFormatting.DARK_RED),
    TUNGSTEN(ChatFormatting.DARK_GRAY),
    UMBER(ChatFormatting.DARK_RED),
    TITANIUM(ChatFormatting.GRAY),
    VANGUARD(ChatFormatting.YELLOW);

    private final ChatFormatting color;

    ShaftTypes(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting getColor() {
        return this.color;
    }
}
