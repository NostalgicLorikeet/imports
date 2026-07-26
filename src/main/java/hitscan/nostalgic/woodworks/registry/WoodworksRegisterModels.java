package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.blocks.BlockPodium;
import hitscan.nostalgic.woodworks.blocks.BlockShelf;
import hitscan.nostalgic.woodworks.client.render.blocks.ModelPodium;
import hitscan.nostalgic.woodworks.client.render.blocks.ModelShelf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class WoodworksRegisterModels {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Block block : WoodworksBlocks.BLOCKS) {
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(block),
                    0,
                    new ModelResourceLocation(block.getRegistryName(), "inventory")
            );
        }

        for (int i = 0; i < 16; i++) {
            String color = EnumDyeColor.byMetadata(i).getTranslationKey();

            ModelLoader.setCustomModelResourceLocation(
                    WoodworksItems.DYED_GAS_TUBE,
                    i,
                    new ModelResourceLocation(WoodworksItems.DYED_GAS_TUBE.getRegistryName() + "_" + color, "inventory")
            );
        }

        ModelLoader.setCustomModelResourceLocation(
                WoodworksItems.GLYPH_TEMPLATE,
                0,
                new ModelResourceLocation(WoodworksItems.GLYPH_TEMPLATE.getRegistryName(), "inventory")
        );
    }

    @SubscribeEvent
    public static void bakeModels(ModelBakeEvent event) {
        DefaultStateMapper defaultStateMapper = new DefaultStateMapper();

        for (BlockShelf shelf : WoodworksBlocks.SHELVES) {
            for (IBlockState state : shelf.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(shelf.getRegistryName(), stateName);
                event.getModelRegistry().putObject(mrl, new ModelShelf(event.getModelRegistry().getObject(mrl)));
            }

            ModelResourceLocation mrl = new ModelResourceLocation(shelf.getRegistryName(), "inventory");
            event.getModelRegistry().putObject(mrl, new ModelShelf(event.getModelRegistry().getObject(mrl)));
        }

        for (BlockPodium podium : WoodworksBlocks.PODIUMS) {
            for (IBlockState state : podium.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(podium.getRegistryName(), stateName);
                event.getModelRegistry().putObject(mrl, new ModelPodium(event.getModelRegistry().getObject(mrl)));
            }

            ModelResourceLocation mrl = new ModelResourceLocation(podium.getRegistryName(), "inventory");
            event.getModelRegistry().putObject(mrl, new ModelPodium(event.getModelRegistry().getObject(mrl)));
        }
    }
}
