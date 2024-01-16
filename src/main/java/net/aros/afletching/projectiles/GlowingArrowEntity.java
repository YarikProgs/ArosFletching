package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.items.GlowingArrowItem;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class GlowingArrowEntity extends PersistentProjectileEntity {
    public int lightLevel = 0;
    private BlockPos lightBlockPos = null;

    public GlowingArrowEntity(EntityType<GlowingArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public GlowingArrowEntity(World world, LivingEntity owner, int lightLevel) {
        super(ModEntityTypes.GLOWING_ARROW, owner, world);
        this.lightLevel = lightLevel;
    }

    @Override
    public void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        BlockPos pos = blockHitResult.getBlockPos();
        if (!world.getBlockState(pos).isAir())
            for (Direction dir : Direction.values()) if (world.getBlockState(pos.offset(dir)).isAir()) {
                pos = pos.offset(dir);
                break;
            }
        if (world.getBlockState(pos).isAir()) {
            lightBlockPos = pos;
            world.setBlockState(pos, Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, lightLevel));
        }
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        boolean res = super.tryPickup(player);
        if (res && lightBlockPos != null) removeLight();
        return res;
    }

    @Override
    public void onRemoved() {
        removeLight();
    }

    private void removeLight() {
        world.setBlockState(lightBlockPos, Blocks.AIR.getDefaultState());
    }

    @Override
    protected ItemStack asItemStack() {
        ItemStack arrow = new ItemStack(ModItems.GLOWING_ARROW);
        arrow.getOrCreateNbt().putInt(GlowingArrowItem.LIGHT_LEVEL, lightLevel);
        return arrow;
    }
}
