package net.aros.afletching.projectiles;

import net.aros.afletching.ArosFletching;
import net.aros.afletching.ArosFletchingClient;
import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.items.GlowingArrowItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class GlowingArrowEntity extends PersistentProjectileEntity {
    public int lightLevel = 0;

    public GlowingArrowEntity(EntityType<GlowingArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public GlowingArrowEntity(World world, LivingEntity owner, int lightLevel) {
        super(ModEntityTypes.GLOWING_ARROW, owner, world);
        this.lightLevel = lightLevel;
    }

    @Override
    public void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos pos = blockHitResult.getBlockPos();
        if (!world.getBlockState(pos).isAir()) {
            for (Direction dir : Direction.values()) if (world.getBlockState(pos.offset(dir)).isAir()) {
                pos = pos.offset(dir);
                break;
            }
        }
        for (PlayerEntity p : world.getPlayers(TargetPredicate.DEFAULT, null, getBoundingBox().expand(100)))
            if (p instanceof ServerPlayerEntity sp) {
                ServerPlayNetworking.send(sp, ArosFletchingClient.ARROW_PARTICLES, PacketByteBufs.create().writeBlockPos(getBlockPos()));
            }
        if (world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, lightLevel));
            discard();
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }
}
