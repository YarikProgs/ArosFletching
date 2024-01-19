package net.aros.afletching.items;

import net.aros.afletching.projectiles.TerracottaArrowEntity;
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

public class TerracottaArrowItem extends ArrowItem {
    public TerracottaArrowItem() {
        super(new FabricItemSettings().maxCount(64).group(ItemGroup.COMBAT));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("♦ ").append(Text.translatable("terracotta_arrow." + MOD_ID + ".has_shards.p1")).formatted(Formatting.GOLD));
        tooltip.add(Text.literal("  ").append(Text.translatable("terracotta_arrow." + MOD_ID + ".has_shards.p2")).formatted(Formatting.GOLD));
        tooltip.add(Text.literal("♦ ").append(Text.translatable("terracotta_arrow." + MOD_ID + ".breaks")).formatted(Formatting.RED));

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new TerracottaArrowEntity(world, shooter);
    }
}
