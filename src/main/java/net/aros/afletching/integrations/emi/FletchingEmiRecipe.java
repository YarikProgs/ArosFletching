package net.aros.afletching.integrations.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.aros.afletching.recipes.FletchingRecipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.aros.afletching.ArosFletching.MOD_ID;

public class FletchingEmiRecipe implements EmiRecipe {
    private final FletchingRecipe recipe;

    public FletchingEmiRecipe(FletchingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ArosFletchingEmi.FLETCHING_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        List<EmiIngredient> inputs = new ArrayList<>();

        inputs.add(EmiIngredient.of(recipe.getFletching()));
        inputs.add(EmiIngredient.of(recipe.getShaft()));
        inputs.add(EmiIngredient.of(recipe.getArrowhead()));
        inputs.add(EmiIngredient.of(recipe.getIngredient1()));
        inputs.add(EmiIngredient.of(recipe.getIngredient2()));

        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(recipe.getOutput()));
    }

    @Override
    public int getDisplayWidth() {
        return 140;
    }

    @Override
    public int getDisplayHeight() {
        return 60;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(getInputs().get(0), 10, 39);
        widgets.addSlot(getInputs().get(1), 28, 21);
        widgets.addSlot(getInputs().get(2), 46, 3);
        widgets.addSlot(getInputs().get(3), 28, 3);
        widgets.addSlot(getInputs().get(4), 46, 21);

        widgets.addTexture(new EmiTexture(new Identifier(MOD_ID, "textures/gui/container/fletching_table/arrows.png"), 0, 0, 26, 15, 26, 15, 26, 15), 70, 15);

        widgets.addSlot(getOutputs().get(0), 100, 11).large(true).recipeContext(this);
    }
}
