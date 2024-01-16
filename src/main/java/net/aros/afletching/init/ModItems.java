package net.aros.afletching.init;

import net.aros.afletching.items.GlowingArrowItem;
import net.aros.afletching.items.TntArrowItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModItems {
    public static final TntArrowItem TNT_ARROW = register("tnt_arrow", new TntArrowItem());
    public static final GlowingArrowItem GLOWING_ARROW = register("glowing_arrow", new GlowingArrowItem());

    static <I extends Item> I register(String name, I item) {
        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), item);
    }

    public static void init() {}
}
