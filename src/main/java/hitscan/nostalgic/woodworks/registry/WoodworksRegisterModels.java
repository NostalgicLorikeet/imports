package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.blocks.*;
import hitscan.nostalgic.woodworks.client.render.blocks.*;
import hitscan.nostalgic.woodworks.client.render.items.ModelNeonGlyph;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
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

        for (int i = 0; i < 16; i++) {
            ModelLoader.setCustomModelResourceLocation(
                    WoodworksItems.NEON_GLYPH,
                    i,
                    new ModelResourceLocation(WoodworksItems.NEON_GLYPH.getRegistryName(), "inventory")
            );
        }

        ModelLoader.setCustomModelResourceLocation(
                WoodworksItems.TEST,
                0,
                new ModelResourceLocation(WoodworksItems.TEST.getRegistryName(), "inventory")
        );

        ModelLoader.setCustomModelResourceLocation(
                WoodworksItems.CRATE_FRAMING,
                0,
                new ModelResourceLocation(WoodworksItems.CRATE_FRAMING.getRegistryName(), "inventory")
        );
    }

    @SubscribeEvent
    public static void bakeModels(ModelBakeEvent event) {
        ModelResourceLocation glyphMRL = new ModelResourceLocation(
            WoodworksItems.NEON_GLYPH.getRegistryName(), "inventory"
        );

        IBakedModel neonGlyphModel = event.getModelRegistry().getObject(glyphMRL);
        if (neonGlyphModel != null) {
            event.getModelRegistry().putObject(glyphMRL, new ModelNeonGlyph(neonGlyphModel));
        }

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

        for (BlockGlyphHolder glyphHolder : WoodworksBlocks.GLYPH_HOLDERS) {
            for (IBlockState state : glyphHolder.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(glyphHolder.getRegistryName(), stateName);
                event.getModelRegistry().putObject(mrl, new ModelGlyphHolder(event.getModelRegistry().getObject(mrl), glyphHolder.getDim()));
            }
        }

        for (BlockGlyphDummy glyphDummy : WoodworksBlocks.GLYPH_HOLDER_DUMMIES) {
            for (IBlockState state : glyphDummy.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(glyphDummy.getRegistryName(), stateName);
                if (glyphDummy == WoodworksBlocks.GLYPH_1X1) ModelGlyphHolder.GLYPH_HOLDER_1x1_MODELS.put(state.getValue(BlockGlyphDummy.FACING), event.getModelRegistry().getObject(mrl));
                if (glyphDummy == WoodworksBlocks.GLYPH_2X2) ModelGlyphHolder.GLYPH_HOLDER_2x2_MODELS.put(state.getValue(BlockGlyphDummy.FACING), event.getModelRegistry().getObject(mrl));
                if (glyphDummy == WoodworksBlocks.GLYPH_4X4) ModelGlyphHolder.GLYPH_HOLDER_4x4_MODELS.put(state.getValue(BlockGlyphDummy.FACING), event.getModelRegistry().getObject(mrl));
            }
        }

        IBakedModel signpostModel =  new ModelSignpost();

        for (BlockSignpost signpost : WoodworksBlocks.SIGNPOSTS) {
            for (IBlockState state : signpost.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(signpost.getRegistryName(), stateName);
                event.getModelRegistry().putObject(mrl, signpostModel);
            }
        }

        for (IBlockState state : WoodworksBlocks.CRATE_WOODEN.getBlockState().getValidStates()) {
            String stateName = defaultStateMapper.getPropertyString(state.getProperties());

            ModelResourceLocation mrl = new ModelResourceLocation(WoodworksBlocks.CRATE_WOODEN.getRegistryName(), stateName);
            event.getModelRegistry().putObject(mrl, new ModelCrate(event.getModelRegistry().getObject(mrl)));
        }

        ModelResourceLocation crateMrl = new ModelResourceLocation(WoodworksBlocks.CRATE_WOODEN.getRegistryName(), "inventory");
        event.getModelRegistry().putObject(crateMrl, new ModelCrate(event.getModelRegistry().getObject(crateMrl)));
    }
}
