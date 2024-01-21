package net.aros.afletching.init;

import net.aros.afletching.ArosFletching;
import net.aros.afletching.items.*;
import net.aros.afletching.projectiles.GlowingArrowEntity;
import net.aros.afletching.projectiles.MessengerArrowEntity;
import net.aros.afletching.projectiles.TntArrowEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModItems {
    public static final TntArrowItem TNT_ARROW = register("tnt_arrow", new TntArrowItem());
    public static final GlowingArrowItem GLOWING_ARROW = register("glowing_arrow", new GlowingArrowItem());
    public static final PrismarineArrowItem RRISMARINE_ARROW = register("prismarine_arrow", new PrismarineArrowItem());
    public static final TerracottaArrowItem TERRACOTTA_ARROW = register("terracotta_arrow", new TerracottaArrowItem());
    public static final BeginnerArrowItem BEGINNER_ARROW = register("beginner_arrow", new BeginnerArrowItem());
    public static final MessengerArrowItem MESSENGER_ARROW = register("messenger_arrow", new MessengerArrowItem());
    public static final CrimsonArrowItem CRIMSON_ARROW = register("crimson_arrow", new CrimsonArrowItem());
    public static final WarpedArrowItem WARPED_ARROW = register("warped_arrow", new WarpedArrowItem());

    static <I extends Item> I register(String name, I item) {
        return register(name, item, true);
    }

    static <I extends Item> I register(String name, I item, boolean registerDispenserBehavior) {
        if (registerDispenserBehavior) {
            DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
                @Override
                protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                    try {
                        PersistentProjectileEntity proj = (PersistentProjectileEntity) Registry.ENTITY_TYPE.get(new Identifier(MOD_ID, name)).create(world);
                        if (proj == null) return null;
                        proj.setPosition(position.getX(), position.getY(), position.getZ());
                        proj.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;

                        if (proj instanceof TntArrowEntity tntArrow) {
                            tntArrow.setExplosionPower(TntArrowItem.getExplosionPower(stack));
                            tntArrow.setBreakingBlocks(TntArrowItem.isBreakingBlocks(stack));
                        }
                        if (proj instanceof GlowingArrowEntity glowingArrow) {
                            glowingArrow.setLightLevel(GlowingArrowItem.getLightLevel(stack));
                        }
                        if (proj instanceof MessengerArrowEntity messengerArrow) {
                            messengerArrow.setItems(stack.getOrCreateNbt().getList(MessengerArrowItem.ITEMS_KEY, NbtElement.COMPOUND_TYPE));
                        }

                        return proj;
                    } catch (Exception e) {
                        ArosFletching.LOGGER.error("Exception while creating projectile for dispenser, message: {}", e.getMessage());
                        return null;
                    }
                }
            });
        }

        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), item);
    }

    public static void init() {}
}
