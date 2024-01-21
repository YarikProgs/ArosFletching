package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEffects;
import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
public class SightArrowEntity extends PersistentProjectileEntity {
    public SightArrowEntity(EntityType<SightArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public SightArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.SIGHT_ARROW, owner, world);
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof PlayerEntity player && !world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(ModEffects.ALL_SEEING, 120 * 20));
            super.onEntityHit(entityHitResult);
            return;
        }
        if (entityHitResult.getEntity() instanceof EnderDragonEntity || !(entityHitResult.getEntity() instanceof MobEntity mob)) {
            if (!world.isClient) world.createExplosion(this, getX(), getY(), getZ(), 1.5F, Explosion.DestructionType.DESTROY);
            return;
        }

        mob.setPersistent();
        super.onEntityHit(entityHitResult);
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
        int c = ModEffects.ALL_SEEING.getColor();
        double d = (double)(c >> 16 & 0xFF) / 255.0;
        double e = (double)(c >> 8 & 0xFF) / 255.0;
        double f = (double)(c & 0xFF) / 255.0;
        for (int j = 0; j < amount; ++j) {
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), d, e, f);
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.SIGHT_ARROW);
    }
}
