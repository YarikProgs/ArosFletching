package net.aros.afletching.items;

import net.aros.afletching.projectiles.TntArrowEntity;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class TntArrowItem extends ArrowItem {
    public static final String IS_BREAKING_BLOCKS = "BreakingBlocks";
    public static final String EXPLOSION_POWER = "ExplosionPower";

    public TntArrowItem() {
        super(new FabricItemSettings().maxCount(64).group(ItemGroup.COMBAT));
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new TntArrowEntity(world, shooter, getExplosionPower(stack), isBreakingBlocks(stack));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        float power = getExplosionPower(stack);
        boolean breaking = isBreakingBlocks(stack);

        tooltip.add(Text.literal("♦ ").append(Text.translatable("tnt_arrow." + MOD_ID + ".explosion_power").append(Text.literal(": "))).formatted(Formatting.GRAY).append(Text.literal(power+"").formatted(Formatting.GOLD)));
        tooltip.add(Text.literal("♦ ").append(Text.translatable("tnt_arrow." + MOD_ID + ".breaking_blocks").append(Text.literal(": "))).formatted(Formatting.GRAY).append(Text.literal(breaking+"").formatted(breaking ? Formatting.GREEN : Formatting.RED)));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public static float getExplosionPower(@NotNull ItemStack stack) {
        return stack.getOrCreateNbt().getFloat(EXPLOSION_POWER);
    }

    public static boolean isBreakingBlocks(@NotNull ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean(IS_BREAKING_BLOCKS);
    }
}
