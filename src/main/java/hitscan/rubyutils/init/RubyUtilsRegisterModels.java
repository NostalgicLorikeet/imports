package hitscan.rubyutils.init;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.blocks.BlockGlyphDummy;
import hitscan.rubyutils.blocks.BlockGlyphHolder;
import hitscan.rubyutils.blocks.BlockPodium;
import hitscan.rubyutils.blocks.BlockShelf;
import hitscan.rubyutils.client.render.blocks.ModelCrate;
import hitscan.rubyutils.client.render.blocks.ModelGlyphHolder;
import hitscan.rubyutils.client.render.blocks.ModelPodium;
import hitscan.rubyutils.client.render.blocks.ModelShelf;
import hitscan.rubyutils.client.render.items.ModelNeonGlyph;
import hitscan.rubyutils.config.RubyUtilsConfig;
import hitscan.rubyutils.statues.EnumStatueType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RubyUtilsRegisterModels {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Block block : RubyUtilsBlocks.BLOCKS) {
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(block),
                    0,
                    new ModelResourceLocation(block.getRegistryName(), "inventory")
            );
        }

        for (int i = 0; i < 16; i++) {
            String color = EnumDyeColor.byMetadata(i).getTranslationKey();

            ModelLoader.setCustomModelResourceLocation(
                    RubyUtilsItems.DYED_GAS_TUBE,
                    i,
                    new ModelResourceLocation(RubyUtilsItems.DYED_GAS_TUBE.getRegistryName() + "_" + color, "inventory")
            );
        }

        for (int i = 0; i < 16; i++) {
            ModelLoader.setCustomModelResourceLocation(
                    RubyUtilsItems.NEON_GLYPH,
                    i,
                    new ModelResourceLocation(RubyUtilsItems.NEON_GLYPH.getRegistryName(), "inventory")
            );
        }

        for (EnumStatueType type : EnumStatueType.values()) {
            ModelLoader.setCustomModelResourceLocation(
                    RubyUtilsItems.STATUE,
                    type.getStatueMeta(),
                    new ModelResourceLocation(new ResourceLocation(Tags.MOD_ID, type.getStatueName() + "_statue"), "inventory")
            );
        }

        ModelLoader.setCustomModelResourceLocation(
                RubyUtilsItems.TRACKLAYER_BOOTS,
                0,
                new ModelResourceLocation(new ResourceLocation(Tags.MOD_ID, "tracklayer_boots"), "inventory")
        );
    }

    @SubscribeEvent
    public static void bakeModels(ModelBakeEvent event) {
        ModelResourceLocation glyphMRL = new ModelResourceLocation(
            RubyUtilsItems.NEON_GLYPH.getRegistryName(), "inventory"
        );

        IBakedModel neonGlyphModel = event.getModelRegistry().getObject(glyphMRL);
        if (neonGlyphModel != null) {
            event.getModelRegistry().putObject(glyphMRL, new ModelNeonGlyph(neonGlyphModel));
        }

        DefaultStateMapper defaultStateMapper = new DefaultStateMapper();

        for (BlockShelf shelf : RubyUtilsBlocks.SHELVES) {
            for (IBlockState state : shelf.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(shelf.getRegistryName(), stateName);
                event.getModelRegistry().putObject(mrl, new ModelShelf(event.getModelRegistry().getObject(mrl)));
            }

            ModelResourceLocation mrl = new ModelResourceLocation(shelf.getRegistryName(), "inventory");
            event.getModelRegistry().putObject(mrl, new ModelShelf(event.getModelRegistry().getObject(mrl)));
        }

        for (BlockPodium podium : RubyUtilsBlocks.PODIUMS) {
            for (IBlockState state : podium.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(podium.getRegistryName(), stateName);
                event.getModelRegistry().putObject(mrl, new ModelPodium(event.getModelRegistry().getObject(mrl)));
            }

            ModelResourceLocation mrl = new ModelResourceLocation(podium.getRegistryName(), "inventory");
            event.getModelRegistry().putObject(mrl, new ModelPodium(event.getModelRegistry().getObject(mrl)));
        }

        for (BlockGlyphHolder glyphHolder : RubyUtilsBlocks.GLYPH_HOLDERS) {
            for (IBlockState state : glyphHolder.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(glyphHolder.getRegistryName(), stateName);
                event.getModelRegistry().putObject(mrl, new ModelGlyphHolder(event.getModelRegistry().getObject(mrl), glyphHolder.getDimensions()));
            }
        }

        for (BlockGlyphDummy glyphDummy : RubyUtilsBlocks.GLYPH_HOLDER_DUMMIES) {
            for (IBlockState state : glyphDummy.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(glyphDummy.getRegistryName(), stateName);
                if (glyphDummy == RubyUtilsBlocks.GLYPH_1X1) ModelGlyphHolder.GLYPH_HOLDER_1x1_MODELS.put(state.getValue(BlockGlyphDummy.FACING), event.getModelRegistry().getObject(mrl));
                if (glyphDummy == RubyUtilsBlocks.GLYPH_2X2) ModelGlyphHolder.GLYPH_HOLDER_2x2_MODELS.put(state.getValue(BlockGlyphDummy.FACING), event.getModelRegistry().getObject(mrl));
                if (glyphDummy == RubyUtilsBlocks.GLYPH_4X4) ModelGlyphHolder.GLYPH_HOLDER_4x4_MODELS.put(state.getValue(BlockGlyphDummy.FACING), event.getModelRegistry().getObject(mrl));
            }
        }

        if (RubyUtilsConfig.crateOptions.enableCrates) {
            for (IBlockState state : RubyUtilsBlocks.CRATE_WOODEN.getBlockState().getValidStates()) {
                String stateName = defaultStateMapper.getPropertyString(state.getProperties());

                ModelResourceLocation mrl = new ModelResourceLocation(RubyUtilsBlocks.CRATE_WOODEN.getRegistryName(), stateName);
                event.getModelRegistry().putObject(mrl, new ModelCrate(event.getModelRegistry().getObject(mrl)));
            }

            ModelResourceLocation crateMrl = new ModelResourceLocation(RubyUtilsBlocks.CRATE_WOODEN.getRegistryName(), "inventory");
            event.getModelRegistry().putObject(crateMrl, new ModelCrate(event.getModelRegistry().getObject(crateMrl)));
        }
    }
}
