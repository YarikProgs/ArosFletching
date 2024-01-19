package net.aros.afletching.projectiles;

import net.aros.afletching.init.ModEntityTypes;
import net.aros.afletching.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BeginnerArrowEntity extends PersistentProjectileEntity {
    public BeginnerArrowEntity(EntityType<BeginnerArrowEntity> type, World world) {
        super(type, world);
        setDamage(.7f);
    }

    public BeginnerArrowEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.BEGINNER_ARROW, owner, world);
        setDamage(.7f);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.BEGINNER_ARROW);
    }
}
