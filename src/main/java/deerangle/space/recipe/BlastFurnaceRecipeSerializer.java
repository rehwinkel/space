package deerangle.space.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BlastFurnaceRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BlastFurnaceRecipe> {

    @Override
    public BlastFurnaceRecipe read(ResourceLocation recipeId, JsonObject json) {
        JsonElement jsonelement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
        Ingredient ingredient = Ingredient.deserialize(jsonelement);
        if (!json.has("result"))
            throw new JsonSyntaxException("Missing result, expected to find a string or object");
        ItemStack resultStack;
        if (json.get("result").isJsonObject())
            resultStack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        else {
            String result = JSONUtils.getString(json, "result");
            ResourceLocation resourcelocation = new ResourceLocation(result);
            resultStack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + result + " does not exist")));
        }
        int duration = JSONUtils.getInt(json, "duration", 0);
        return new BlastFurnaceRecipe(recipeId, ingredient, resultStack, duration);
    }

    @Override
    public BlastFurnaceRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.read(buffer);
        ItemStack resultStack = buffer.readItemStack();
        int duration = buffer.readVarInt();
        return new BlastFurnaceRecipe(recipeId, ingredient, resultStack, duration);
    }

    @Override
    public void write(PacketBuffer buffer, BlastFurnaceRecipe recipe) {
        recipe.ingredient.write(buffer);
        buffer.writeItemStack(recipe.result);
        buffer.writeVarInt(recipe.duration);
    }

}
