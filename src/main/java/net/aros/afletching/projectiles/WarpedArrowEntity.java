package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEffects;
import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class WarpedArrowEntity extends PersistentProjectileEntity {
    public WarpedArrowEntity(EntityType<WarpedArrowEntity> type, World world) {
        super(type, world);
    }

    public WarpedArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.WARPED_ARROW, owner, world);
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
        int c = ModEffects.CONFUSION.getColor();
        double d = (double)(c >> 16 & 0xFF) / 255.0;
        double e = (double)(c >> 8 & 0xFF) / 255.0;
        double f = (double)(c & 0xFF) / 255.0;
        for (int j = 0; j < amount; ++j) {
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), d, e, f);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity l && l.isMobOrPlayer()) {
            l.removeStatusEffect(ModEffects.RAGE);
            l.addStatusEffect(new StatusEffectInstance(ModEffects.CONFUSION, 10 * 20));
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.WARPED_ARROW);
    }
}
