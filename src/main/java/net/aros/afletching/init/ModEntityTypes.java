package net.aros.afletching.init;

import net.aros.afletching.projectiles.GlowingArrowEntity;
import net.aros.afletching.projectiles.PrismarineArrowEntity;
import net.aros.afletching.projectiles.TntArrowEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModEntityTypes {
    public static final EntityType<TntArrowEntity> TNT_ARROW = register("tnt_arrow", createArrow(TntArrowEntity::new));
    public static final EntityType<GlowingArrowEntity> GLOWING_ARROW = register("glowing_arrow", createArrow(GlowingArrowEntity::new));
    public static final EntityType<PrismarineArrowEntity> PRISMARINE_ARROW = register("prismarine_arrow", createArrow(PrismarineArrowEntity::new));

    private static <I extends PersistentProjectileEntity> EntityType<I> createArrow(EntityType.EntityFactory<I> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(new EntityDimensions(.5f, .5f, true)).trackRangeBlocks(4).trackedUpdateRate(20).build();
    }
    static <I extends EntityType<?>> I register(String name, I obj) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, name), obj);
    }

    public static void init() {}
}
