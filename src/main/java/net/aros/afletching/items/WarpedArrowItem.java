package net.aros.afletching.items;

import net.aros.afletching.projectiles.WarpedArrowEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.gui.screen.Screen;
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

public class WarpedArrowItem extends ArrowItem {
    public WarpedArrowItem() {
        super(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(64).rarity(Rarity.RARE));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("♦ ").append(Text.translatable("warped_arrow." + MOD_ID + ".effect")).formatted(Formatting.GRAY));
        tooltip.add(Text.empty());

        if (!Screen.hasShiftDown())
            tooltip.add(Text.literal("[SHIFT]").formatted(Formatting.GRAY));
        else for (int i = 0; i < 5; i++)
            tooltip.add(Text.translatable("effect." + MOD_ID + ".confusion.desc" + i).formatted(Formatting.GRAY));


        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new WarpedArrowEntity(world, shooter);
    }
}
