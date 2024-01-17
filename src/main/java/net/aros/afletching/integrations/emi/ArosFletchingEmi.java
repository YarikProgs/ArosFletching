package net.aros.afletching.integrations.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.aros.afletching.recipes.FletchingRecipe;
import net.minecraft.block.Blocks;

public class ArosFletchingEmi implements EmiPlugin {
    public static final EmiStack FLETCHING_WORKSTATION = EmiStack.of(Blocks.FLETCHING_TABLE);
    public static final EmiRecipeCategory FLETCHING_CATEGORY = new EmiRecipeCategory(FLETCHING_WORKSTATION.getId(), FLETCHING_WORKSTATION);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(FLETCHING_CATEGORY);
        registry.addWorkstation(FLETCHING_CATEGORY, FLETCHING_WORKSTATION);

        for (FletchingRecipe recipe : registry.getRecipeManager().listAllOfType(FletchingRecipe.Type.INSTANCE))
            registry.addRecipe(new FletchingEmiRecipe(recipe));
    }
}
