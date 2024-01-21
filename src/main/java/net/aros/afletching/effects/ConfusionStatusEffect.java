package net.aros.afletching.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WardenEntity;

public class ConfusionStatusEffect extends StatusEffect {
    public ConfusionStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x399692);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof MobEntity mob && !mob.world.isClient) {
            System.out.println("G: " + mob.goalSelector.getGoals());
            System.out.println("T: " + mob.targetSelector.getGoals());
            mob.goalSelector = new GoalSelector(mob.world.getProfilerSupplier());
            mob.targetSelector = new GoalSelector(mob.world.getProfilerSupplier());
            mob.initGoals();
        }
        if (entity instanceof WardenEntity warden) warden.updateAnger();
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof MobEntity mob && !mob.world.isClient) {
            mob.setTarget(null);
            mob.goalSelector.clear();
            mob.targetSelector.clear();
        }
    }
}
