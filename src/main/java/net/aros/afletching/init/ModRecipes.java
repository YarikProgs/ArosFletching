package net.aros.afletching.init;

import net.aros.afletching.recipes.FletchingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class ModRecipes {
    public static void init() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, FletchingRecipe.Serializer.ID),
                FletchingRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, FletchingRecipe.Type.ID),
                FletchingRecipe.Type.INSTANCE);
    }
}
