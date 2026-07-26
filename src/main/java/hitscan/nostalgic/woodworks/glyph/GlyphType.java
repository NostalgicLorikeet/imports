package hitscan.nostalgic.woodworks.glyph;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum GlyphType {
    OPENBLOCKS_ASCII("OpenBlocks ASCII");

    private String name;

    GlyphType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
