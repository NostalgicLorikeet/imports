package hitscan.nostalgic.woodworks.glyphs;

public enum EnumGlyphColor {
    WHITE(0xFFFFFFFF, 0x7FFFFFFF),
    ORANGE(0xFF007FFF, 0x7F007FFF),
    MAGENTA(0xFFFF00FF, 0x7FFF00FF),
    LIGHT_BLUE(0xFFFFA663, 0x7FFFA663),
    YELLOW(0xFF00FFFF, 0x7F00FFFF),
    LIME(0xFF00FF00, 0x7F00FF00),
    PINK(0xFFD3AEFF, 0x7FD3AEFF),
    GRAY(0xFF7F7F7F, 0x7F7F7F7F),
    SILVER(0xFFBFBFBF, 0x7FBFBFBF),
    CYAN(0xFFFFFF00, 0x7FFFFF00),
    PURPLE(0xFFFF009D, 0x7FFF009D),
    BLUE(0xFFFF0000, 0x7FFF0000),
    BROWN(0xFF004A9F, 0x7F004A9F),
    GREEN(0xFF007F00, 0x7F007F00),
    RED(0xFF0000FF, 0x7F0000FF),
    BLACK(0xFF100010, 0x7F100010);

    public final int colorInt;
    public final int colorIntTransparent;

    EnumGlyphColor(int colorInt, int colorIntTransparent) {
        this.colorInt = colorInt;
        this.colorIntTransparent = colorIntTransparent;
    }
}
