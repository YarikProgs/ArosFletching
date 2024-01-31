package net.aros.afletching.mixin;

import net.aros.afletching.init.ModEffects;
import net.aros.afletching.init.ModSounds;
import net.aros.afletching.network.DoWhistleC2SPacket;
import net.aros.afletching.util.WhistleData;
import net.aros.afletching.util.WhistleHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;
    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Nullable public ClientWorld world;

    @Redirect(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I"))
    private void setSlotRedirect(@NotNull PlayerInventory instance, int value) {
        if (!checkPlayer(instance.player)) instance.selectedSlot = value;
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenInject(Screen screen, CallbackInfo ci) {
        if (screen instanceof HandledScreen<?> && currentScreen == null && checkPlayer(player)) ci.cancel();
    }

    @Redirect(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean isEmptyRedirect(ItemStack instance) {
        if (!instance.isEmpty()) return false;

        if (player != null && world != null && player.isSneaking()) {
            WhistleData data = WhistleHelper.getWhistleData(player);
            if (!data.isEmpty()) {
                player.swingHand(Hand.MAIN_HAND);
                PacketByteBuf buf = PacketByteBufs.create().writeUuid(data.target());
                buf.writeInt(data.stack());
                ClientPlayNetworking.send(DoWhistleC2SPacket.ID, buf);
                player.playSound(findEntity(world, data.target()) == null ? SoundEvents.ENTITY_VILLAGER_NO :  ModSounds.ENTITY_PLAYER_WHISTLE,
                        SoundCategory.PLAYERS, 1f, 1.2f / (player.getRandom().nextFloat() * 0.2f + 0.9f));
                WhistleHelper.clearWhistle(player);
            }
        }
        return true;
    }

    @Unique
    private static @Nullable LivingEntity findEntity(@NotNull ClientWorld world, UUID uuid) {
        for (Entity e : world.getEntities()) if (Objects.equals(e.getUuid(), uuid) && e instanceof LivingEntity livingEntity) return livingEntity;
        return null;
    }

    @Unique
    private static boolean checkPlayer(PlayerEntity player) {
        return player != null && !player.isCreative() && player.hasStatusEffect(ModEffects.CONFUSION);
    }
}
