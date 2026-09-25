package hitscan.nostalgic.woodworks.proxy;

import hitscan.nostalgic.woodworks.registry.WoodworksTileEntities;

public class CommonProxy {
    public void preInit() {
        WoodworksTileEntities.regsiterTileEntities();
    }

    public void init() {

    }
}
