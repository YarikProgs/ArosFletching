package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEffects;
import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.util.IErasableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class OblivionArrowEntity extends PersistentProjectileEntity {
    public OblivionArrowEntity(EntityType<OblivionArrowEntity> type, World world) {
        super(type, world);
    }

    public OblivionArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.OBLIVION_ARROW, owner, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof PlayerEntity player && !world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(ModEffects.CONFUSION, 9999999));
            super.onEntityHit(entityHitResult);
            return;
        }
        if (entityHitResult.getEntity() instanceof EnderDragonEntity || !(entityHitResult.getEntity() instanceof IErasableEntity entity)) {
            if (!world.isClient) world.createExplosion(this, getX(), getY(), getZ(), 1, Explosion.DestructionType.DESTROY);
            return;
        }

        super.onEntityHit(entityHitResult);
        entity.afletching_erase();
    }

    @Override
    public void tick() {
        super.tick();
        if (world.isClient) {
            if (inGround) if (inGroundTime % 5 == 0) addParticles(1);
            else addParticles(2);
        }
    }

    private void addParticles(int amount) {
        for (int j = 0; j < amount; ++j) {
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0, 0, 0);
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.OBLIVION_ARROW);
    }
}
