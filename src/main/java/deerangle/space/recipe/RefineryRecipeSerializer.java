package deerangle.space.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class RefineryRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RefineryRecipe> {

    @Override
    public RefineryRecipe read(ResourceLocation recipeId, JsonObject json) {
        if (!json.has("input"))
            throw new JsonSyntaxException("Missing input, expected to find an object");
        FluidStack input = readFluidStack(JSONUtils.getJsonObject(json, "input"));
        if (!json.has("result"))
            throw new JsonSyntaxException("Missing result, expected to find an object");
        FluidStack result = readFluidStack(JSONUtils.getJsonObject(json, "result"));
        int duration = JSONUtils.getInt(json, "duration");
        return new RefineryRecipe(recipeId, input, result, duration);
    }

    private static FluidStack readFluidStack(JsonObject result) {
        int amount = JSONUtils.getInt(result, "amount", 1);
        String fluidName = JSONUtils.getString(result, "fluid");
        IForgeRegistry<Fluid> registry = RegistryManager.ACTIVE.getRegistry(Fluid.class);
        Fluid fluid = registry.getValue(new ResourceLocation(fluidName));
        assert fluid != null;
        return new FluidStack(fluid, amount);
    }

    public static JsonElement writeFluidStack(FluidStack stack) {
        JsonObject el = new JsonObject();
        el.addProperty("fluid", stack.getFluid().getRegistryName().toString());
        el.addProperty("amount", stack.getAmount());
        return el;
    }

    @Override
    public RefineryRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        FluidStack input = buffer.readFluidStack();
        FluidStack result = buffer.readFluidStack();
        int duration = buffer.readInt();
        return new RefineryRecipe(recipeId, input, result, duration);
    }

    @Override
    public void write(PacketBuffer buffer, RefineryRecipe recipe) {
        buffer.writeFluidStack(recipe.getInputFluid());
        buffer.writeFluidStack(recipe.getResultFluid());
        buffer.writeInt(recipe.getDuration());
    }

}
