package net.aros.afletching.init;

import net.aros.afletching.projectiles.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModEntityTypes {
    public static final EntityType<TntArrowEntity> TNT_ARROW = register("tnt_arrow", createProjectile(TntArrowEntity::new));
    public static final EntityType<GlowingArrowEntity> GLOWING_ARROW = register("glowing_arrow", createProjectile(GlowingArrowEntity::new));
    public static final EntityType<PrismarineArrowEntity> PRISMARINE_ARROW = register("prismarine_arrow", createProjectile(PrismarineArrowEntity::new));
    public static final EntityType<TerracottaArrowEntity> TERRACOTTA_ARROW = register("terracotta_arrow", createProjectile(TerracottaArrowEntity::new));
    public static final EntityType<BeginnerArrowEntity> BEGINNER_ARROW = register("beginner_arrow", createProjectile(BeginnerArrowEntity::new));
    public static final EntityType<MessengerArrowEntity> MESSENGER_ARROW = register("messenger_arrow", createProjectile(MessengerArrowEntity::new));
    public static final EntityType<CrimsonArrowEntity> CRIMSON_ARROW = register("crimson_arrow", createProjectile(CrimsonArrowEntity::new));
    public static final EntityType<WarpedArrowEntity> WARPED_ARROW = register("warped_arrow", createProjectile(WarpedArrowEntity::new));
    public static final EntityType<OblivionArrowEntity> OBLIVION_ARROW = register("oblivion_arrow", createProjectile(OblivionArrowEntity::new));
    public static final EntityType<SightArrowEntity> SIGHT_ARROW = register("sight_arrow", createProjectile(SightArrowEntity::new));
    public static final EntityType<CeramicShardEntity> CERAMIC_SHARD = register("ceramic_shard", createProjectile(CeramicShardEntity::new, .5f, .2f));
    public static final EntityType<EnchantedFeatherEntity> ENCHANTED_FEATHER = register("enchanted_feather", createProjectile(EnchantedFeatherEntity::new));

    private static <I extends PersistentProjectileEntity> EntityType<I> createProjectile(EntityType.EntityFactory<I> factory) {
        return createProjectile(factory, .5f, .5f);
    }

    private static <I extends PersistentProjectileEntity> EntityType<I> createProjectile(EntityType.EntityFactory<I> factory, float width, float height) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(new EntityDimensions(width, height, true)).trackRangeBlocks(4).trackedUpdateRate(20).build();
    }

    static <I extends EntityType<?>> I register(String name, I obj) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, name), obj);
    }

    public static void init() {}
}
