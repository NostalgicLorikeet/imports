package hitscan.rubyutils.init;

import hitscan.rubyutils.Tags;
import hitscan.rubyutils.config.RubyUtilsConfig;
import hitscan.rubyutils.entity.EntityDollStatue;
import hitscan.rubyutils.entity.EntityEncrusted;
import hitscan.rubyutils.entity.EntityStoneStatue;
import hitscan.rubyutils.entity.EntityStrawStatue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class RubyUtilsEntities {
    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
        if (RubyUtilsConfig.statueOptions.enableStatues) {
            EntityEntry strawStatue = EntityEntryBuilder.create()
                    .entity(EntityStrawStatue.class)
                    .name("straw_statue")
                    .id(new ResourceLocation(Tags.MOD_ID, "straw_statue"), 0)
                    .tracker(64, 3, true)
                    .build();

            ForgeRegistries.ENTITIES.register(strawStatue);

            EntityEntry stoneStatue = EntityEntryBuilder.create()
                    .entity(EntityStoneStatue.class)
                    .name("stone_statue")
                    .id(new ResourceLocation(Tags.MOD_ID, "stone_statue"), 1)
                    .tracker(64, 3, true)
                    .build();

            ForgeRegistries.ENTITIES.register(stoneStatue);

            EntityEntry doll = EntityEntryBuilder.create()
                    .entity(EntityDollStatue.class)
                    .name("doll_statue")
                    .id(new ResourceLocation(Tags.MOD_ID, "doll_statue"), 2)
                    .tracker(64, 3, true)
                    .build();

            ForgeRegistries.ENTITIES.register(doll);
        }

        if (RubyUtilsConfig.encrustedOptions.enableEncrusted) {
            EntityEntry encrusted = EntityEntryBuilder.create()
                    .entity(EntityEncrusted.class)
                    .name("encrusted")
                    .id(new ResourceLocation(Tags.MOD_ID, "encrusted"), 3)
                    .tracker(64, 3, true)
                    .egg(3092279, 5329233)
                    .build();

            ForgeRegistries.ENTITIES.register(encrusted);
        }
    }
}
