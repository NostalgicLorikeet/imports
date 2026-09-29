package hitscan.nostalgic.woodworks.registry;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.entity.EntityDollStatue;
import hitscan.nostalgic.woodworks.entity.EntityStoneStatue;
import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class WoodworksEntities {
    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
        EntityEntry strawStatue = EntityEntryBuilder.create()
                .entity(EntityStrawStatue.class)
                .name("straw_statue")
                .id(new ResourceLocation(Tags.MOD_ID,"straw_statue"), 0)
                .tracker(64, 3, true)
                .build();

        ForgeRegistries.ENTITIES.register(strawStatue);

        EntityEntry stoneStatue = EntityEntryBuilder.create()
                .entity(EntityStoneStatue.class)
                .name("stone_statue")
                .id(new ResourceLocation(Tags.MOD_ID,"stone_statue"), 1)
                .tracker(64, 3, true)
                .build();

        ForgeRegistries.ENTITIES.register(stoneStatue);

        EntityEntry doll = EntityEntryBuilder.create()
                .entity(EntityDollStatue.class)
                .name("doll_statue")
                .id(new ResourceLocation(Tags.MOD_ID,"doll_statue"), 2)
                .tracker(64, 3, true)
                .build();

        ForgeRegistries.ENTITIES.register(doll);
    }
}
