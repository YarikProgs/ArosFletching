package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.items.EnchantedFeatherItem;
import net.aros.afletching.network.WhistleS2CPacket;
import net.aros.afletching.util.WhistleHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EnchantedFeatherEntity extends PersistentProjectileEntity {
    public static final TrackedData<Boolean> HURT_TARGET = DataTracker.registerData(EnchantedFeatherEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public EnchantedFeatherEntity(EntityType<EnchantedFeatherEntity> type, World world) {
        super(type, world);
    }

    public EnchantedFeatherEntity(World world, PlayerEntity owner, boolean hurtTarget) {
        super(ModEntityTypes.ENCHANTED_FEATHER, owner, world);
        setHurtingTarget(hurtTarget);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(HURT_TARGET, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HurtTarget", isHurtingTarget());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setHurtingTarget(nbt.getBoolean("HurtTarget"));
    }

    public void setHurtingTarget(boolean hurtTarget) {
        dataTracker.set(HURT_TARGET, hurtTarget);
    }

    public boolean isHurtingTarget() {
        return dataTracker.get(HURT_TARGET);
    }

    @Override
    protected void onEntityHit(EntityHitResult hitRes) {
        discard();
        if (hitRes.getEntity() instanceof LivingEntity entity && getOwner() instanceof ServerPlayerEntity player) {

            WhistleHelper.whistleFor(player, entity, isHurtingTarget());
            PacketByteBuf buf = PacketByteBufs.create().writeUuid(entity.getUuid());
            buf.writeBoolean(isHurtingTarget());
            ServerPlayNetworking.send(player, WhistleS2CPacket.ID, buf);
        } else {
            ItemEntity item = EntityType.ITEM.create(world);
            if (item != null) {
                item.setPosition(getPos());
                item.setStack(asItemStack());
                world.spawnEntity(item);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        discard();
        ItemEntity item = EntityType.ITEM.create(world);
        if (item != null) {
            item.setPosition(getPos());
            item.setStack(asItemStack());
            world.spawnEntity(item);
        }
    }

    @Override
    protected ItemStack asItemStack() {
        ItemStack stack = new ItemStack(ModItems.ENCHANTED_FEATHER);
        if (isHurtingTarget()) stack.getOrCreateNbt().putBoolean(EnchantedFeatherItem.HURT_TARGET, true);
        return stack;
    }
}
