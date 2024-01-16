package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.items.TntArrowItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;

public class TntArrowEntity extends PersistentProjectileEntity {
    public static final TrackedData<Float> EXPLOSION_POWER = DataTracker.registerData(TntArrowEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Boolean> BREAKING_BLOCKS = DataTracker.registerData(TntArrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    
    public TntArrowEntity(EntityType<TntArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public TntArrowEntity(World world, LivingEntity owner, float explosionPower, boolean breakingBlocks) {
        super(ModEntityTypes.TNT_ARROW, owner, world);
        setExplosionPower(explosionPower);
        setBreakingBlocks(breakingBlocks);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(EXPLOSION_POWER, 1F);
        dataTracker.startTracking(BREAKING_BLOCKS, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat(TntArrowItem.EXPLOSION_POWER, getExplosionPower());
        nbt.putBoolean(TntArrowItem.IS_BREAKING_BLOCKS, isBreakingBlocks());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setExplosionPower(nbt.getFloat(TntArrowItem.EXPLOSION_POWER));
        setBreakingBlocks(nbt.getBoolean(TntArrowItem.IS_BREAKING_BLOCKS));
    }

    public void setExplosionPower(float power) {
        dataTracker.set(EXPLOSION_POWER, power);
    }

    public float getExplosionPower() {
        return dataTracker.get(EXPLOSION_POWER);
    }

    public void setBreakingBlocks(boolean breakingBlocks) {
        dataTracker.set(BREAKING_BLOCKS, breakingBlocks);
    }

    public boolean isBreakingBlocks() {
        return dataTracker.get(BREAKING_BLOCKS);
    }

    @Override
    protected void onHit(@NotNull LivingEntity target) {
        world.createExplosion(this, target.getX(), target.getY(), target.getZ(), getExplosionPower(),
                isBreakingBlocks() ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE);
        discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        Vec3d pos = blockHitResult.getPos();
        world.createExplosion(this, pos.x, pos.y, pos.z, getExplosionPower(), isBreakingBlocks()
                ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE);
        discard();
    }

    @Override
    protected ItemStack asItemStack() {
        ItemStack stack = new ItemStack(ModItems.TNT_ARROW);
        stack.getOrCreateNbt().putBoolean(TntArrowItem.IS_BREAKING_BLOCKS, isBreakingBlocks());
        stack.getOrCreateNbt().putFloat(TntArrowItem.EXPLOSION_POWER, getExplosionPower());
        return stack;
    }
}
