package net.aros.afletching.mixin;

import net.aros.afletching.effects.RageStatusEffect;
import net.aros.afletching.init.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickInject(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        for (StatusEffectInstance effect : self.getStatusEffects()) {
            if (effect.getEffectType() == ModEffects.RAGE) ((RageStatusEffect) effect.getEffectType()).playerTick(self);
        }
    }
}
