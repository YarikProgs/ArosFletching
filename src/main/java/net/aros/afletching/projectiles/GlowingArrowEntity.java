package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.items.GlowingArrowItem;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class GlowingArrowEntity extends PersistentProjectileEntity {
    public static final TrackedData<Integer> LIGHT_LEVEL = DataTracker.registerData(GlowingArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Optional<BlockPos>> LIGHT_BLOCK_POS = DataTracker.registerData(GlowingArrowEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);

    public GlowingArrowEntity(EntityType<GlowingArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public GlowingArrowEntity(World world, LivingEntity owner, int lightLevel) {
        super(ModEntityTypes.GLOWING_ARROW, owner, world);
        setLightLevel(lightLevel);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(LIGHT_LEVEL, 0);
        dataTracker.startTracking(LIGHT_BLOCK_POS, Optional.empty());
    }

    public void setLightLevel(int power) {
        dataTracker.set(LIGHT_LEVEL, power);
    }

    public int getLightLevel() {
        return dataTracker.get(LIGHT_LEVEL);
    }

    public void setLightBlockPos(BlockPos pos) {
        dataTracker.set(LIGHT_BLOCK_POS, Optional.ofNullable(pos));
    }

    public Optional<BlockPos> getLightBlockPos() {
        return dataTracker.get(LIGHT_BLOCK_POS);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt(GlowingArrowItem.LIGHT_LEVEL, getLightLevel());
        Optional<BlockPos> opt = getLightBlockPos();
        nbt.put("LightPos", opt.map(NbtHelper::fromBlockPos).orElseGet(NbtCompound::new));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setLightLevel(nbt.getInt(GlowingArrowItem.LIGHT_LEVEL));

        NbtCompound blockPosNbt = nbt.getCompound("LightPos");
        setLightBlockPos(blockPosNbt.isEmpty() ? null : NbtHelper.toBlockPos(blockPosNbt));
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
            setLightBlockPos(pos);
            world.setBlockState(pos, Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, getLightLevel()));
        }
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        boolean res = super.tryPickup(player);
        if (res) removeLight();
        return res;
    }

    @Override
    public void onRemoved() {
        removeLight();
    }

    private void removeLight() {
        if (getLightBlockPos().isPresent())
            world.setBlockState(getLightBlockPos().get(), Blocks.AIR.getDefaultState());
    }

    @Override
    protected ItemStack asItemStack() {
        ItemStack arrow = new ItemStack(ModItems.GLOWING_ARROW);
        arrow.getOrCreateNbt().putInt(GlowingArrowItem.LIGHT_LEVEL, getLightLevel());
        return arrow;
    }
}
