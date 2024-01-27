package net.aros.afletching.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
    @Inject(method = "outputFromJson", at = @At("HEAD"), cancellable = true)
    private static void outputFromJsonInject(JsonObject json, CallbackInfoReturnable<ItemStack> cir) {
        Item item = ShapedRecipe.getItem(json);
        if (json.has("data")) throw new JsonParseException("Disallowed data tag found");
        int i = JsonHelper.getInt(json, "count", 1);
        if (i < 1) throw new JsonSyntaxException("Invalid output count: " + i);
        ItemStack output = new ItemStack(item, i);
        if (json.has("nbt")) {
            String nbtString = json.get("nbt").isJsonNull() ? null : json.get("nbt").getAsJsonObject().toString();
            try {
                if (nbtString != null) output.setNbt(StringNbtReader.parse(nbtString));
            } catch (CommandSyntaxException ignored) {}
        }
        cir.setReturnValue(output);
    }
}
