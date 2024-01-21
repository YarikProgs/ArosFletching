package net.aros.afletching.mixin;

import net.aros.afletching.effects.RageStatusEffect;
import net.aros.afletching.init.ModEffects;
import net.aros.afletching.util.IErasableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements IErasableEntity {
    @Shadow
    public abstract void setTarget(@Nullable LivingEntity target);

    @Shadow
    public abstract EntityNavigation getNavigation();

    @Shadow
    public GoalSelector targetSelector;
    @Shadow
    public GoalSelector goalSelector;

    @Shadow
    public abstract @Nullable LivingEntity getTarget();

    @Unique
    private MobEntity self = (MobEntity) (Object) this;
    @Unique
    private static final TrackedData<Boolean> NEED_TO_BE_ERASED = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "tick", at = @At("TAIL"))
    @SuppressWarnings("deprecation")
    private void tickInject(CallbackInfo ci) {
        for (StatusEffectInstance effect : self.getStatusEffects()) {
            if (effect.getEffectType() == ModEffects.RAGE) ((RageStatusEffect) effect.getEffectType()).tick(self);
        }
        if (self == null) self = (MobEntity) (Object) this;

        if (isNeedToBeErased()) {
            if (!self.getBrain().getMemories().isEmpty()) self.getBrain().clear();
            if (!goalSelector.getGoals().isEmpty()) goalSelector.clear();
            if (!targetSelector.getGoals().isEmpty()) targetSelector.clear();
            if (getNavigation().isFollowingPath()) getNavigation().stop();
            if (getTarget() != null) setTarget(null);
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTrackerInject(CallbackInfo ci) {
        if (self == null) self = (MobEntity) (Object) this;
        self.getDataTracker().startTracking(NEED_TO_BE_ERASED, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtInject(@NotNull NbtCompound nbt, CallbackInfo ci) {
        if (self == null) self = (MobEntity) (Object) this;
        nbt.putBoolean("NeedToBeErased", self.getDataTracker().get(NEED_TO_BE_ERASED));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtInject(@NotNull NbtCompound nbt, CallbackInfo ci) {
        if (self == null) self = (MobEntity) (Object) this;
        self.getDataTracker().set(NEED_TO_BE_ERASED, nbt.getBoolean("NeedToBeErased"));
    }

    @Override
    public void afletching_erase() {
        if (self == null) self = (MobEntity) (Object) this;
        setNeedToBeErased(true);
        self.getBrain().clear();
        goalSelector.clear();
        targetSelector.clear();
        getNavigation().stop();
        setTarget(null);
    }

    @Unique
    public void setNeedToBeErased(boolean need) {
        self.getDataTracker().set(NEED_TO_BE_ERASED, need);
    }

    @Unique
    public boolean isNeedToBeErased() {
        return self.getDataTracker().get(NEED_TO_BE_ERASED);
    }
}
