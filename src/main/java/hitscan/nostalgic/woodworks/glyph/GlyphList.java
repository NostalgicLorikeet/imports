package hitscan.nostalgic.woodworks.glyph;

import java.util.HashMap;

public class GlyphList {
    private static final String asciiList = "";
    private static final String unicodeList = "";
    private static final String sgaList = "";

    private static final HashMap<Character, Long> asciiBits = new HashMap<>();
    private static final HashMap<Character, long[]> unicodeBits = new HashMap<>();
    private static final HashMap<Character, Long> sgaBits = new HashMap<>();

    public static long[] getCharacterBits(char character, GlyphType glyphType) {
        long[] list = new long[glyphType == GlyphType.UNICODE ? 4 : 1];

        if (glyphType == GlyphType.ASCII) {
            list[0] = asciiBits.get(character);
        } else if (glyphType == GlyphType.SGA) {
            list[0] = sgaBits.get(character);
        } else {
            for (int i = 0; i < 4; i++) {
                list = unicodeBits.get(character);
            }
        }

        return list;
    }
}
