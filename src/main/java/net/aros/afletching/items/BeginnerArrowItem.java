package net.aros.afletching.items;

import net.aros.afletching.projectiles.BeginnerArrowEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class BeginnerArrowItem extends ArrowItem {
    public BeginnerArrowItem() {
        super(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(64));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("♦ ").append(Text.translatable("beginner_arrow." + MOD_ID + ".damage")).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new BeginnerArrowEntity(world, shooter);
    }
}
