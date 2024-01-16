package net.aros.afletching.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.aros.afletching.ArosFletching;
import net.aros.afletching.screen.FletchingScreenHandler;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;


public class FletchingRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final Ingredient arrowhead;
    private final Ingredient shaft;
    private final Ingredient fletching;
    private final Ingredient ingredient1;
    private final Ingredient ingredient2;

    public FletchingRecipe(Identifier id, ItemStack output, Ingredient arrowhead, Ingredient shaft, Ingredient fletching, Ingredient ingredient1, Ingredient ingredient2) {
        this.id = id;
        this.output = output;
        this.arrowhead = arrowhead;
        this.shaft = shaft;
        this.fletching = fletching;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient) return false;

        return arrowhead.test(inventory.getStack(FletchingScreenHandler.ARROWHEAD)) &&
               shaft.test(inventory.getStack(FletchingScreenHandler.SHAFT)) &&
               fletching.test(inventory.getStack(FletchingScreenHandler.FLETCHING)) &&
               (ingredient1.test(inventory.getStack(FletchingScreenHandler.INGREDIENT_1)) &&
               ingredient2.test(inventory.getStack(FletchingScreenHandler.INGREDIENT_2)) ||
                       ingredient1.test(inventory.getStack(FletchingScreenHandler.INGREDIENT_2)) &&
                               ingredient2.test(inventory.getStack(FletchingScreenHandler.INGREDIENT_1)));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<FletchingRecipe> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<FletchingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "fletching";

        @Override
        public FletchingRecipe read(Identifier id, JsonObject json) {
            JsonObject ingredients = json.getAsJsonObject("ingredients");
            JsonObject outputJson = json.getAsJsonObject("output");
            ItemStack output = new ItemStack(
                    Registry.ITEM.get(new Identifier(outputJson.getAsJsonPrimitive("id").getAsString())),
                    outputJson.getAsJsonPrimitive("count").getAsInt()
            );
            if (outputJson.has("nbt")) {
                String nbtString = outputJson.get("nbt").isJsonNull() ? "{}" : outputJson.get("nbt").getAsJsonObject().toString();

                try {
                    output.setNbt(StringNbtReader.parse(nbtString));
                } catch (CommandSyntaxException e) {
                    ArosFletching.LOGGER.error("Cannot read recipe \"{}\". Error: {}", id.toString(), e.getMessage());
                }
            }
            Ingredient arrowhead = Ingredient.fromJson(ingredients.getAsJsonObject("arrowhead"));
            Ingredient shaft = Ingredient.fromJson(ingredients.getAsJsonObject("shaft"));
            Ingredient fletching = Ingredient.fromJson(ingredients.getAsJsonObject("fletching"));

            JsonElement ingredient1json = ingredients.get("ingredient1");
            JsonElement ingredient2json = ingredients.get("ingredient2");

            Ingredient ingredient1 = ingredient1json == null || ingredient1json.isJsonNull() ?
                    Ingredient.EMPTY : Ingredient.fromJson(ingredient1json.getAsJsonObject());
            Ingredient ingredient2 = ingredient2json == null || ingredient2json.isJsonNull() ?
                    Ingredient.EMPTY : Ingredient.fromJson(ingredient2json.getAsJsonObject());

            return new FletchingRecipe(id, output, arrowhead, shaft, fletching, ingredient1, ingredient2);
        }

        @Override
        public FletchingRecipe read(Identifier id, PacketByteBuf buf) {
            return new FletchingRecipe(id, buf.readItemStack(), Ingredient.fromPacket(buf), Ingredient.fromPacket(buf),
                    Ingredient.fromPacket(buf), Ingredient.fromPacket(buf), Ingredient.fromPacket(buf));
        }

        @Override
        public void write(PacketByteBuf buf, FletchingRecipe recipe) {
            buf.writeItemStack(recipe.output);
            recipe.arrowhead.write(buf);
            recipe.shaft.write(buf);
            recipe.fletching.write(buf);
            recipe.ingredient1.write(buf);
            recipe.ingredient2.write(buf);
        }
    }

    public Ingredient getArrowhead() {
        return arrowhead;
    }

    public Ingredient getShaft() {
        return shaft;
    }

    public Ingredient getFletching() {
        return fletching;
    }

    public Ingredient getIngredient1() {
        return ingredient1;
    }

    public Ingredient getIngredient2() {
        return ingredient2;
    }

    public static class Type implements RecipeType<FletchingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "fletching";
        private Type() {
        }
    }
}
