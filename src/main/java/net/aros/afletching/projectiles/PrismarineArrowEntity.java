package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PrismarineArrowEntity extends PersistentProjectileEntity {
    public PrismarineArrowEntity(EntityType<PrismarineArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public PrismarineArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.PRISMARINE_ARROW, owner, world);
    }

    @Override
    protected float getDragInWater() {
        return 1;
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.RRISMARINE_ARROW);
    }
}
