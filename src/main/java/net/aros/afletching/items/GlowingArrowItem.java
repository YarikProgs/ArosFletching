package net.aros.afletching.items;

import net.aros.afletching.projectiles.GlowingArrowEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class GlowingArrowItem extends ArrowItem {
    public static final String LIGHT_LEVEL = "LightLevel";

    public GlowingArrowItem() {
        super(new FabricItemSettings().maxCount(64).group(ItemGroup.COMBAT));
    }

    @Override
    public GlowingArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new GlowingArrowEntity(world, shooter, getLightLevel(stack));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int lightLevel = getLightLevel(stack);

        tooltip.add(Text.literal("♦ ").append(Text.translatable("glowing_arrow." + MOD_ID + ".light_level").append(Text.literal(": "))).formatted(Formatting.GRAY).append(Text.literal(lightLevel+"").formatted(Formatting.GOLD)));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public static int getLightLevel(ItemStack stack) {
        return stack.getOrCreateNbt().getInt(LIGHT_LEVEL);
    }
}
