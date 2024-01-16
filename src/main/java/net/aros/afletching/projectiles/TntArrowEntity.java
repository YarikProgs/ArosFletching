package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.items.TntArrowItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;

public class TntArrowEntity extends PersistentProjectileEntity {
    public float explosionPower = 1;
    public boolean breakingBlocks = false;

    public TntArrowEntity(EntityType<TntArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public TntArrowEntity(World world, LivingEntity owner, float explosionPower, boolean breakingBlocks) {
        super(ModEntityTypes.TNT_ARROW, owner, world);
        this.explosionPower = explosionPower;
        this.breakingBlocks = breakingBlocks;
    }

    @Override
    protected void onHit(@NotNull LivingEntity target) {
        world.createExplosion(this, target.getX(), target.getY(), target.getZ(), explosionPower,
                breakingBlocks ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE);
        discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        Vec3d pos = blockHitResult.getPos();
        world.createExplosion(this, pos.x, pos.y, pos.z, explosionPower, breakingBlocks
                ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE);
        discard();
    }

    @Override
    protected ItemStack asItemStack() {
        ItemStack stack = new ItemStack(ModItems.TNT_ARROW);
        stack.getOrCreateNbt().putBoolean(TntArrowItem.IS_BREAKING_BLOCKS, breakingBlocks);
        stack.getOrCreateNbt().putFloat(TntArrowItem.EXPLOSION_POWER, explosionPower);
        return stack;
    }
}
