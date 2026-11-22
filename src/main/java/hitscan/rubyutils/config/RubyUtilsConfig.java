package hitscan.rubyutils.config;

import hitscan.rubyutils.Tags;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID, name = Tags.MOD_ID)
public class RubyUtilsConfig {
    @Config.Comment("Config options for shelves")
    @Config.Name("Shelves")
    @Config.RequiresMcRestart
    public static ShelfOptions shelfOptions = new ShelfOptions();
    @Config.Comment("Config options for podiums")
    @Config.Name("Podiums")
    @Config.RequiresMcRestart
    public static PodiumOptions podiumOptions = new PodiumOptions();
    @Config.Comment("Config options for neon glyphs")
    @Config.Name("Neon Glyphs")
    @Config.RequiresMcRestart
    public static NeonGlyphHolderOptions neonGlyphHolderOptions = new NeonGlyphHolderOptions();
    @Config.Comment("Config options for crates")
    @Config.Name("Crates")
    @Config.RequiresMcRestart
    public static CrateOptions crateOptions = new CrateOptions();
    @Config.Comment("Config options for statues")
    @Config.Name("Statues")
    @Config.RequiresMcRestart
    public static StatueOptions statueOptions = new StatueOptions();
    @Config.Comment("Config options for the Encrusted")
    @Config.Name("Encrusted")
    @Config.RequiresMcRestart
    public static EncrustedOptions encrustedOptions = new EncrustedOptions();
    @Config.Comment("Config options for the Tracklayer Boots")
    @Config.Name("Tracklayer Boots")
    @Config.RequiresMcRestart
    public static TracklayerBootsOptions tracklayerBootsOptions = new TracklayerBootsOptions();

    public static class ShelfOptions {
        @Config.Comment({"Enable shelves.", "Default: true"})
        public boolean enableShelves = true;
    }

    public static class PodiumOptions {
        @Config.Comment({"Enable podiums.", "Default: true"})
        public boolean enablePodiums = true;
    }

    public static class NeonGlyphHolderOptions {
        @Config.Comment({"Enable neon glyph holders.", "Default: true"})
        public boolean enableNeonGlyphHolders = true;

        @Config.Comment({"Enable dyed gas tubes.", "Default: true"})
        public boolean enableDyedGasTubes = true;
    }

    public static class CrateOptions {
        @Config.Comment({"Enable crates.", "Default: true"})
        public boolean enableCrates = true;
    }

    public static class StatueOptions {
        @Config.Comment({"Enable statues.", "Default: true"})
        public boolean enableStatues = true;
    }

    public static class EncrustedOptions {
        @Config.Comment({"Enable Encrusted.", "Default: true"})
        public boolean enableEncrusted = true;
    }

    public static class TracklayerBootsOptions {
        @Config.Comment({"Enable Tracklayer Boots.", "Default: true"})
        public boolean enableTracklayerBoots = true;

        @Config.Comment({"Tracklayer Boots durability.", "Default: true"})
        public int tracklayerBootsDurability = 143;
    }
}
