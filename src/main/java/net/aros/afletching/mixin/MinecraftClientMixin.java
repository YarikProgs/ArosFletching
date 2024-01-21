package net.aros.afletching.mixin;

import net.aros.afletching.init.ModEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Nullable public ClientPlayerEntity player;

//    @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1))
//    private void setScreenRedirect(@NotNull MinecraftClient instance, Screen screen) {
//        if (instance.player == null || !instance.player.hasStatusEffect(ModEffects.CONFUSION)) instance.setScreen(screen);
//    }
//
    @Redirect(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I"))
    private void setSlotRedirect(@NotNull PlayerInventory instance, int value) {
        if (!checkPlayer(instance.player)) instance.selectedSlot = value;
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenInject(Screen screen, CallbackInfo ci) {
        if (screen != null && !(screen instanceof GameModeSelectionScreen) && currentScreen == null && checkPlayer(player)) ci.cancel();
    }

    @Unique
    static boolean checkPlayer(PlayerEntity player) {
        return player != null && !player.isCreative() && player.hasStatusEffect(ModEffects.CONFUSION);
    }
}
