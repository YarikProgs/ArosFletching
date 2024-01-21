package net.aros.afletching.items;

import net.aros.afletching.projectiles.OblivionArrowEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class OblivionArrowItem extends ArrowItem {
    public OblivionArrowItem() {
        super(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(64).rarity(Rarity.EPIC));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("♦ ").append(Text.translatable("item." + MOD_ID + ".oblivion_arrow.desc0")).formatted(Formatting.YELLOW));
        tooltip.add(Text.literal("♦ ").append(Text.translatable("item." + MOD_ID + ".oblivion_arrow.desc1")).formatted(Formatting.YELLOW));
        tooltip.add(Text.literal("  ").append(Text.translatable("item." + MOD_ID + ".oblivion_arrow.desc2")).formatted(Formatting.YELLOW));

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new OblivionArrowEntity(world, shooter);
    }
}
