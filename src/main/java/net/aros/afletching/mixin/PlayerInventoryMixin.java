package net.aros.afletching.mixin;

import net.aros.afletching.init.ModEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    private void scrollInject(double scrollAmount, CallbackInfo ci) {
        if (checkPlayer(player)) ci.cancel();
    }

    @Unique
    private static boolean checkPlayer(PlayerEntity player) {
        return player != null && !player.isCreative() && player.hasStatusEffect(ModEffects.CONFUSION);
    }
}
