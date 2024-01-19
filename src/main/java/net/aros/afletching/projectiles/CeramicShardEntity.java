package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.Objects;

public class CeramicShardEntity extends PersistentProjectileEntity {
    private final Entity ignoredEntity;

    public CeramicShardEntity(EntityType<CeramicShardEntity> type, World world) {
        super(type, world);
        ignoredEntity = null;
        setPierceLevel((byte) 3);
    }

    public CeramicShardEntity(World world, double x, double y, double z, Entity entity) {
        super(ModEntityTypes.CERAMIC_SHARD, x, y, z, world);
        ignoredEntity = entity;
        setPierceLevel((byte) 3);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        setSound(ModSounds.TERRACOTTA_ARROW_ENTITY_HIT);
        super.onEntityHit(entityHitResult);
    }

    @Override
    public boolean canHit(Entity other) {
        return super.canHit(other) && !Objects.equals(other, ignoredEntity);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        setSound(ModSounds.TERRACOTTA_ARROW_BLOCK_HIT);
        super.onBlockHit(blockHitResult);
        discard();
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }
}
