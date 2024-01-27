package net.aros.afletching.items;

import net.aros.afletching.projectiles.EnchantedFeatherEntity;
import net.aros.afletching.util.WhistleData;
import net.aros.afletching.util.WhistleHelper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class EnchantedFeatherItem extends Item {
    public static final String HURT_TARGET = "HurtTarget";

    public EnchantedFeatherItem() {
        super(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("♦ ").append(Text.translatable("enchanted_feather." + MOD_ID + ".desc0")).formatted(Formatting.GRAY));
        tooltip.add(Text.literal("  ").append(Text.translatable("enchanted_feather." + MOD_ID + ".desc1")).formatted(Formatting.GRAY));

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null && isHurtingTarget(stack)) {
            WhistleData whistleData = WhistleHelper.getWhistleData(client.player);
            int playerStack = whistleData.isEmpty() ? 0 : whistleData.stack();

            tooltip.add(Text.literal("♦ ").append(Text.translatable("enchanted_feather." + MOD_ID + ".hurts", playerStack)).formatted(Formatting.RED));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;
        float pullProgress;
        if ((double)(pullProgress = getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks)) < 0.1) return;
        if (!world.isClient) {
            EnchantedFeatherEntity feather = createFeather(world, player, stack);
            feather.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, pullProgress * 1.7f, 1.0f);
            world.spawnEntity(feather);
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 4.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
            if (stack.isEmpty()) player.getInventory().removeOne(stack);
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public EnchantedFeatherEntity createFeather(World world, PlayerEntity shooter, ItemStack stack) {
        return new EnchantedFeatherEntity(world, shooter, isHurtingTarget(stack));
    }

    public static float getPullProgress(int useTicks) {
        float f = useTicks / 20.0f;
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) f = 1.0f;
        return f;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    public static boolean isHurtingTarget(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean(HURT_TARGET);
    }
}
