package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.init.ModSounds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TerracottaArrowEntity extends PersistentProjectileEntity {
    public TerracottaArrowEntity(EntityType<TerracottaArrowEntity> type, World world) {
        super(type, world);
    }

    public TerracottaArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.TERRACOTTA_ARROW, owner, world);
    }

    private void onAnyCollision() {
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        setSound(ModSounds.TERRACOTTA_ARROW_BLOCK_HIT);
        super.onBlockHit(blockHitResult);
        onAnyCollision();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        setSound(ModSounds.TERRACOTTA_ARROW_ENTITY_HIT);
        super.onEntityHit(entityHitResult);

        if (!world.isClient) {
            for (int i = 0; i < 3; i++) {
                float pitch, yaw;
                CeramicShardEntity shard = new CeramicShardEntity(world, getX(), getY(), getZ(), entityHitResult.getEntity());
                shard.setOwner(getOwner());
                shard.setPitch(pitch = getPitch());
                shard.setYaw(yaw = getYaw() + (15 * (i - 1)));

                shard.setCritical(random.nextBetween(0, 5) == 0);

                shard.setVelocity(
                        MathHelper.sin(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180)),
                        MathHelper.sin(pitch * ((float)Math.PI / 180)),
                        MathHelper.cos(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180)),
                        1.0f, 1.0f);

                world.spawnEntity(shard);
            }
        }

        onAnyCollision();
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            ParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.TERRACOTTA.getDefaultState());
            for (int i = 0; i < 8; ++i) this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.TERRACOTTA_ARROW);
    }
}
