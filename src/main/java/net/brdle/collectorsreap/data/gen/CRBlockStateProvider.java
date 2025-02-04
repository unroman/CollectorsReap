package net.brdle.collectorsreap.data.gen;

import net.brdle.collectorsreap.Util;
import net.brdle.collectorsreap.CollectorsReap;
import net.brdle.collectorsreap.common.block.*;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.data.BlockStates;

public class CRBlockStateProvider extends BlockStateProvider {
    public CRBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, CollectorsReap.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.pieBlock(CRBlocks.PORTOBELLO_QUICHE);
        this.pieBlock(CRBlocks.LIME_PIE);
        this.stageBlock(CRBlocks.PORTOBELLO_COLONY.get(), PortobelloColonyBlock.COLONY_AGE);
        this.cross(CRBlocks.PORTOBELLO.get());
        this.getVariantBuilder(CRBlocks.SMALL_LIME_BUSH.get())
            .partialState().with(SmallLimeBushBlock.AGE, 0).modelForState().modelFile(existingModel("small_lime_bush_stage0")).addModel()
            .partialState().with(SmallLimeBushBlock.AGE, 1).modelForState().modelFile(models()
                .withExistingParent("block/small_lime_bush_stage1", Util.rl("minecraft", "block/template_azalea"))
                .texture("side", resourceBlock("small_lime_bush_side_stage1"))
                .texture("top", resourceBlock("small_lime_bush_top_stage1"))
                .texture("plant", resourceBlock("small_lime_bush_plant_stage1"))
                .texture("particle", resourceBlock("small_lime_bush_plant_stage1"))
                .renderType("cutout")).addModel();
        this.upperLowerStageBlock(CRBlocks.LIME_BUSH.get(), LimeBushBlock.AGE, LimeBushBlock.HALF, LimeBushBlock.STUNTED);
        this.pomegranateBushBlock(CRBlocks.POMEGRANATE_BUSH.get(), PomegranateBushBlock.AGE, PomegranateBushBlock.HALF);
        this.getVariantBuilder(CRBlocks.SMALL_POMEGRANATE_BUSH.get())
            .partialState().with(SmallPomegranateBushBlock.AGE, 0).modelForState().modelFile(existingModel("small_pomegranate_bush_stage0")).addModel()
            .partialState().with(SmallPomegranateBushBlock.AGE, 1).modelForState().modelFile(existingModel("small_pomegranate_bush_stage1")).addModel();
        this.crateBlock(CRBlocks.LIME_CRATE.get(), "lime");
        this.crateBlock(CRBlocks.POMEGRANATE_CRATE.get(), "pomegranate");
    }

    public void crateBlock(Block block, String cropName) {
        this.simpleBlock(block, this.models().cubeBottomTop(Util.name(block), resourceBlock(cropName + "_crate_side"), Util.rl(FarmersDelight.MODID, "block/crate_bottom"), resourceBlock(cropName + "_crate_top")));
    }

    // Adapted from: https://github.com/vectorwing/FarmersDelight/blob/1.19/src/main/java/vectorwing/farmersdelight/data/BlockStates.java
    public static ResourceLocation resourceBlock(String path) {
        return Util.cr("block/" + path);
    }

    public ModelFile existingModel(String path) {
        return new ModelFile.ExistingModelFile(resourceBlock(path), models().existingFileHelper);
    }

    public void cross(Block block) {
        this.simpleBlock(block, models().cross("block/" + Util.name(block),
            CRBlockStateProvider.resourceBlock(Util.name(block))).renderType("cutout"));
    }

    // Adapted from: https://github.com/vectorwing/FarmersDelight/blob/1.19/src/main/java/vectorwing/farmersdelight/data/BlockStates.java
    public void stageBlock(Block block, IntegerProperty ageProperty, Property<?>... ignored) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            String stageName = Util.name(block) + "_stage" + state.getValue(ageProperty);
            return ConfiguredModel.builder()
                .modelFile(models().cross(stageName, resourceBlock(stageName)).renderType("cutout")).build();
        }, ignored);
    }

    public void upperLowerStageBlock(Block block, IntegerProperty ageProperty, EnumProperty<DoubleBlockHalf> halfProperty, Property<?> ignored) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            String name = Util.name(block) + "_" + state.getValue(halfProperty).getSerializedName();
            var mod = models()
                .withExistingParent("block/" + name + "_stage" + state.getValue(ageProperty), Util.cr(name))
                .texture("side", resourceBlock(Util.name(block) + "_side_stage" + state.getValue(ageProperty)))
                .texture("plant", resourceBlock(Util.name(block) + "_plant_" + state.getValue(halfProperty).getSerializedName() + "_stage" + state.getValue(ageProperty)))
                .texture("particle", resourceBlock(Util.name(block) + "_plant_" + state.getValue(halfProperty).getSerializedName() + "_stage" + state.getValue(ageProperty)));
            if (state.getValue(halfProperty) == DoubleBlockHalf.UPPER) {
                mod.texture("top", resourceBlock(Util.name(block) + "_top_stage" + state.getValue(ageProperty)));
            }
            return ConfiguredModel.builder().modelFile(mod).build();
        }, ignored);
    }

    public void pomegranateBushBlock(Block block, IntegerProperty ageProperty, EnumProperty<DoubleBlockHalf> halfProperty) {
        getVariantBuilder(block).forAllStates(state -> {
            String halfStageName = Util.name(block) + "_" + state.getValue(halfProperty).getSerializedName() + "_stage" + state.getValue(ageProperty);
            String name = Util.name(block) + "_stage" + state.getValue(ageProperty);
            return ConfiguredModel.builder()
                .modelFile(models()
                    .withExistingParent("block/" + halfStageName,
                        Util.cr(Util.name(block) + "_" + state.getValue(halfProperty).getSerializedName()))
                    .texture("0", resourceBlock(name))).build();
        });
    }

    // Adapted from: https://github.com/vectorwing/FarmersDelight/blob/1.19/src/main/java/vectorwing/farmersdelight/data/BlockStates.java
    public void pieBlock(RegistryObject<Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            int bites = state.getValue(PieBlock.BITES);
            String name = Util.name(block);
            String suffix = bites > 0 ? "_slice" + bites : "";
            var mod = models()
                .withExistingParent("block/" + name + suffix, Util.rl(FarmersDelight.MODID, "pie" + suffix))
                .texture("top", resourceBlock(name + "_top"))
                .texture("bottom", resourceBlock(name + "_bottom"))
                .texture("side", resourceBlock(name + "_side"))
                .texture("particle", resourceBlock(name + "_top"));
            if (bites > 0) {
                mod.texture("inner", resourceBlock(name + "_inner"));
            }
            return ConfiguredModel.builder().modelFile(mod)
                .rotationY(((int) state.getValue(PieBlock.FACING).toYRot() + 180) % 360).build();
            }
        );
    }
}
