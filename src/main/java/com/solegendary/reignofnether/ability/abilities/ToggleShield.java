package com.solegendary.reignofnether.ability.abilities;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.monsters.CreeperUnit;
import com.solegendary.reignofnether.unit.units.piglins.PiglinBruteUnit;
import com.solegendary.reignofnether.unit.units.villagers.EvokerUnit;
import com.solegendary.reignofnether.unit.units.villagers.RavagerUnit;
import com.solegendary.reignofnether.util.MyRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;

import java.util.List;

public class ToggleShield extends Ability {

    private static final int CD_MAX_SECONDS = 0;

    private final PiglinBruteUnit bruteUnit;

    public ToggleShield(PiglinBruteUnit bruteUnit) {
        super(
                UnitAction.TOGGLE_SHIELD,
                CD_MAX_SECONDS * ResourceCost.TICKS_PER_SECOND,
                0,
                0,
                false
        );
        this.bruteUnit = bruteUnit;
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton(
                "Shield Stance",
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/items/shield.png"),
                hotkey,
                () -> bruteUnit.isHoldingUpShield,
                () -> false,
                () -> true,
                () -> UnitClientEvents.sendUnitCommand(UnitAction.TOGGLE_SHIELD),
                null,
                List.of(
                        FormattedCharSequence.forward("Shield Stance", Style.EMPTY),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward("Hold up your shield, halving all projectile ", Style.EMPTY),
                        FormattedCharSequence.forward("damage taken but also halving movement speed.", Style.EMPTY)
                ),
                this
        );
    }

    @Override
    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        bruteUnit.isHoldingUpShield = !bruteUnit.isHoldingUpShield;
    }
}
