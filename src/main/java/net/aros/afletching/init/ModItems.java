package net.aros.afletching.init;

import net.aros.afletching.items.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModItems {
    public static final TntArrowItem TNT_ARROW = register("tnt_arrow", new TntArrowItem());
    public static final GlowingArrowItem GLOWING_ARROW = register("glowing_arrow", new GlowingArrowItem());
    public static final PrismarineArrowItem RRISMARINE_ARROW = register("prismarine_arrow", new PrismarineArrowItem());
    public static final TerracottaArrowItem TERRACOTTA_ARROW = register("terracotta_arrow", new TerracottaArrowItem());
    public static final BeginnerArrowItem BEGINNER_ARROW = register("beginner_arrow", new BeginnerArrowItem());
    public static final MessengerArrowItem MESSENGER_ARROW = register("messenger_arrow", new MessengerArrowItem());

    static <I extends Item> I register(String name, I item) {
        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), item);
    }

    public static void init() {}
}
