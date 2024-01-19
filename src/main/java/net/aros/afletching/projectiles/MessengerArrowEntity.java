package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.aros.afletching.items.MessengerArrowItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class MessengerArrowEntity extends PersistentProjectileEntity {
    public static final TrackedData<NbtCompound> ITEMS = DataTracker.registerData(MessengerArrowEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    public MessengerArrowEntity(EntityType<MessengerArrowEntity> type, World world) {
        super(type, world);
        setItems(new NbtCompound());
    }

    public MessengerArrowEntity(World world, LivingEntity owner, NbtList items) {
        super(ModEntityTypes.MESSENGER_ARROW, owner, world);
        setItems(items);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(ITEMS, new NbtCompound());
    }

    @Override
    protected void age() {

    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof PlayerEntity player) {
            ItemStack stack = asItemStack();
            if (player.getInventory().getEmptySlot() == -1) player.dropItem(stack, true);
            else player.giveItemStack(stack);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Items", getItems());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setItems(nbt.getCompound("Items"));
    }

    public void setItems(NbtCompound items) {
        dataTracker.set(ITEMS, items);
    }

    public void setItems(NbtList list) {
        NbtCompound items = new NbtCompound();
        items.put("ItemList", list);
        dataTracker.set(ITEMS, items);
    }

    public NbtCompound getItems() {
        return dataTracker.get(ITEMS);
    }

    @Override
    protected ItemStack asItemStack() {
        ItemStack stack = new ItemStack(ModItems.MESSENGER_ARROW);
        stack.getOrCreateNbt().put(MessengerArrowItem.ITEMS_KEY, getItems().getList("ItemList", NbtElement.COMPOUND_TYPE));
        return stack;
    }
}
