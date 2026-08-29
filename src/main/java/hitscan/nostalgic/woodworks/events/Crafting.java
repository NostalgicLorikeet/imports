package hitscan.nostalgic.woodworks.events;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.recipes.CrateRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Crafting {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        CrateRecipe crateRecipe = new CrateRecipe();
        crateRecipe.setRegistryName(new ResourceLocation(Tags.MOD_ID, "crate"));
        event.getRegistry().register(crateRecipe);
    }
}