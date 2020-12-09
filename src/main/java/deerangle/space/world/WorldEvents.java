package deerangle.space.world;

import deerangle.space.capability.Capabilities;
import deerangle.space.main.SpaceMod;
import deerangle.space.network.PacketHandler;
import deerangle.space.network.SyncWeatherMsg;
import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isRemote()) {
            event.world.getCapability(Capabilities.WEATHER_CAPABILITY).ifPresent(cap -> {
                int current = cap.getCurrentWeatherTimeout();
                cap.setCurrentWeatherTimeout(current - 1);
                if (cap.getAvailableWeathers().size() > 0) {
                    if (current <= 1) {
                        cap.setCurrentWeatherTimeout(cap.getMinWeatherTimeout() + event.world.getRandom().nextInt(cap.getMaxWeatherTimeout() - cap.getMinWeatherTimeout()));
                        int index = event.world.getRandom().nextInt(cap.getAvailableWeathers().size() + 1);
                        if (index == cap.getAvailableWeathers().size()) {
                            cap.setCurrentWeather(null);
                        } else {
                            cap.setCurrentWeather(cap.getAvailableWeathers().get(index).get());
                        }
                        PacketHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(event.world::getDimensionKey), new SyncWeatherMsg(cap.getCurrentWeather()));
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof ServerPlayerEntity) {
            event.getWorld().getCapability(Capabilities.WEATHER_CAPABILITY).ifPresent(cap -> {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getEntity()), new SyncWeatherMsg(cap.getCurrentWeather()));
            });
        }
    }

    private static ConfiguredFeature<?, ?> ALUMINIUM_ORE;
    private static ConfiguredFeature<?, ?> COPPER_ORE;
    private static ConfiguredFeature<?, ?> TITANIUM_ORE;

    public static void registerFeatures() {
        ALUMINIUM_ORE = register("aluminium_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ResourceRegistry.ALUMINIUM_ORE.get().getDefaultState(), 9)).range(256).square().func_242731_b(5));
        COPPER_ORE = register("copper_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ResourceRegistry.COPPER_ORE.get().getDefaultState(), 12)).range(128).square().func_242731_b(20));
        TITANIUM_ORE = register("titanium_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ResourceRegistry.ILMENITE_ORE.get().getDefaultState(), 4)).range(20).square().func_242731_b(2));
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SpaceMod.MOD_ID, key), configuredFeature);
    }

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if (isOverworldBiome(event.getCategory())) {
            withOres(event.getGeneration());
        }
    }

    private static void withOres(BiomeGenerationSettingsBuilder generation) {
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ALUMINIUM_ORE);
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, COPPER_ORE);
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, TITANIUM_ORE);
    }

    public static boolean isOverworldBiome(Biome.Category category) {
        return category != Biome.Category.NONE && category != Biome.Category.THEEND && category != Biome.Category.NETHER;
    }


    @SubscribeEvent
    public static void entityTicking(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().isInWater()) {
            FluidState state = event.getEntity().getEntityWorld().getFluidState(event.getEntity().getPosition());
            if (state.getFluid() == FluidRegistry.ACID.get()) {
                event.getEntityLiving().attackEntityFrom(DamageSources.ACID, 1);
            }
        }
    }

}
