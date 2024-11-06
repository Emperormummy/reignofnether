package com.solegendary.reignofnether.ability.abilities;

import com.solegendary.reignofnether.ability.EnchantAbility;
import com.solegendary.reignofnether.ability.EnchantAbilityServerboundPacket;
import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.buildings.villagers.Library;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.HudClientEvents;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.resources.ResourcesClientEvents;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.villagers.PillagerUnit;
import com.solegendary.reignofnether.util.MyRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EnchantMultishot extends EnchantAbility {

    private static final UnitAction ENCHANT_ACTION = UnitAction.ENCHANT_MULTISHOT;
    private static final Enchantment actualEnchantment = Enchantments.MULTISHOT;

    public EnchantMultishot(Library library) {
        super(ENCHANT_ACTION, library, ResourceCosts.ENCHANT_MULTISHOT);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton(
                "Multishot Enchantment",
                new ResourceLocation("minecraft", "textures/item/crossbow_standby.png"),
                hotkey,
                () -> CursorClientEvents.getLeftClickAction() == ENCHANT_ACTION || library.autoCastEnchant == this,
                () -> false,
                library::isUpgraded,
                () -> CursorClientEvents.setLeftClickAction(ENCHANT_ACTION),
                () -> {
                    EnchantAbilityServerboundPacket.setAutocastEnchant(ENCHANT_ACTION, library.originPos);
                    if (library.autoCastEnchant == this)
                        library.autoCastEnchant = null;
                    else
                        library.autoCastEnchant = this;
                },
                List.of(
                        FormattedCharSequence.forward("Multishot Enchantment", Style.EMPTY.withBold(true)),
                        ResourceCosts.getFormattedCost(cost),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward("Enchant a Pillager's crossbow with Multishot, ", Style.EMPTY),
                        FormattedCharSequence.forward("making it fire 3 arrows at once in a spread.", Style.EMPTY),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward("Right click to auto-cast", Style.EMPTY)
                ),
                this
        );
    }

    @Override
    public boolean isCorrectUnitAndEquipment(LivingEntity entity) {
        return entity instanceof PillagerUnit &&
                entity.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof CrossbowItem;
    }

    @Override
    public boolean hasAnyEnchant(LivingEntity entity) {
        return !entity.getItemBySlot(EquipmentSlot.MAINHAND).getAllEnchantments().isEmpty();
    }

    @Override
    protected boolean hasSameEnchant(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.MAINHAND).getAllEnchantments().containsKey(actualEnchantment);
    }

    @Override
    protected void doEnchant(LivingEntity entity) {
        entity.getItemBySlot(EquipmentSlot.MAINHAND).enchant(actualEnchantment, 1);
    }
}
