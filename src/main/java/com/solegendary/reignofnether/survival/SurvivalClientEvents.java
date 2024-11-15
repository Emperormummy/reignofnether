package com.solegendary.reignofnether.survival;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class SurvivalClientEvents {

    public static int waveNumber = 1;
    public static boolean isEnabled = false;
    public static WaveDifficulty difficulty = WaveDifficulty.EASY;

    private static Minecraft MC = Minecraft.getInstance();

    public static int getMinutesPerDay() {
        return switch (SurvivalClientEvents.difficulty) {
            case EASY -> 10;
            case MEDIUM -> 8;
            case HARD -> 6;
            case EXTREME -> 4;
        };
    }

    public static void enable(WaveDifficulty diff) {
        if (MC.player == null)
            return;

        difficulty = diff;
        isEnabled = true;

        MC.player.sendSystemMessage(Component.literal(""));
        MC.player.sendSystemMessage(Component.translatable(I18n.get("hud.gamemode.reignofnether.survival1"))
                .withStyle(Style.EMPTY.withBold(true)));
        MC.player.sendSystemMessage(Component.translatable(I18n.get("hud.gamemode.reignofnether.survival4",
                difficulty, getMinutesPerDay())));
        MC.player.sendSystemMessage(Component.literal(""));
    }
}
