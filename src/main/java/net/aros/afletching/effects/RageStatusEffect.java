package net.aros.afletching.effects;

import net.aros.afletching.init.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RageStatusEffect extends StatusEffect {
    public RageStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xA63727);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);

        if (!(entity instanceof MobEntity mob)) return;

        for (Goal goal : new ArrayList<>(mob.targetSelector.getGoals())) if (goal instanceof TargetGoal) mob.targetSelector.remove(goal);
    }

    public void tick(@NotNull MobEntity mob) {

        boolean hasTarget = false;
        for (Goal goal : mob.targetSelector.getGoals()) if (goal instanceof TargetGoal) {
            hasTarget = true;
            break;
        }
        if (hasTarget) return;

        mob.targetSelector.add(0, new TargetGoal(mob));
    }

    public void playerTick(PlayerEntity player) {
        for (Entity entity : player.world.getOtherEntities(player, player.getBoundingBox().expand(3, 0, 3)).stream().filter(e -> e instanceof LivingEntity l && l.isMobOrPlayer()).sorted((e1, e2) -> (int) Math.signum(e2.distanceTo(player) - e1.distanceTo(player))).toList()) {
            if (player.age % 4 == 0) {
                ItemStack item = player.getStackInHand(Hand.MAIN_HAND);

                if (!item.isDamageable() || item.getMaxDamage() > item.getDamage() + 1) {
                    player.attack(entity);
                    player.swingHand(Hand.MAIN_HAND);
                    if (player.getRandom().nextFloat() > .8) {
                        if (entity.isAlive()) entity.damage(DamageSource.player(player), 1);
                        player.addCritParticles(entity);
                    }
                } else {
                    entity.damage(DamageSource.player(player), .5f);
                    player.swingHand(Hand.OFF_HAND);
                }

            }
            if (player.age % 10 == 0) player.move(MovementType.SELF, entity.getPos().subtract(player.getPos()).multiply(1f/3f));
        }
        float div = 3.5F;

        player.move(MovementType.SELF, new Vec3d(
                player.getRandom().nextFloat()/div - player.getRandom().nextFloat()/div,
                0,
                player.getRandom().nextFloat()/div - player.getRandom().nextFloat()/div
        ));
    }

    static class TargetGoal extends ActiveTargetGoal<LivingEntity> {
        public TargetGoal(MobEntity mob) {
            super(mob, LivingEntity.class, 0, true, true, LivingEntity::isMobOrPlayer);
        }

        @Override
        public boolean canStart() {
            return mob.hasStatusEffect(ModEffects.RAGE) && super.canStart();
        }

        @Override
        public void start() {
            super.start();
            this.mob.setDespawnCounter(0);
        }
    }
}
