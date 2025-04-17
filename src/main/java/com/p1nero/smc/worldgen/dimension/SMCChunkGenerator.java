package com.p1nero.smc.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.worldgen.biome.SMCBiomeProvider;
import com.p1nero.smc.worldgen.structure.SMCStructurePoses;
import com.p1nero.smc.worldgen.structure.PositionPlacement;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class SMCChunkGenerator extends NoiseBasedChunkGeneratorWrapper {

    public static final Codec<SMCChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ChunkGenerator.CODEC.fieldOf("wrapped_generator").forGetter(o -> o.delegate),
            NoiseGeneratorSettings.CODEC.fieldOf("noise_generation_settings").forGetter(NoiseBasedChunkGenerator::generatorSettings)
    ).apply(instance, SMCChunkGenerator::new));

    public SMCChunkGenerator(ChunkGenerator delegate, Holder<NoiseGeneratorSettings> noiseGeneratorSettingsHolder) {
        super(delegate, noiseGeneratorSettingsHolder);
    }

    @Override
    protected @NotNull Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    /**
     * 从原版抄的，重点在判断StructurePlacement的时候加入自己的判断。如果返回true即可在该位置生成结构。
     */
    @Override
    public void createStructures(@NotNull RegistryAccess pRegistryAccess, ChunkGeneratorStructureState pStructureState, @NotNull StructureManager pStructureManager, ChunkAccess pChunk, @NotNull StructureTemplateManager pStructureTemplateManager) {
        ChunkPos pos = pChunk.getPos();
        SectionPos sectionPos = SectionPos.bottomOf(pChunk);
        RandomState randomState = pStructureState.randomState();
        pStructureState.possibleStructureSets().forEach((value) -> {
            StructurePlacement structurePlacement = (value.value()).placement();
            List<StructureSet.StructureSelectionEntry> iterator = (value.value()).structures();

            for (StructureSet.StructureSelectionEntry $$11 : iterator) {
                StructureStart structureStart = pStructureManager.getStartForStructure(sectionPos, $$11.structure().value(), pChunk);
                if (structureStart != null && structureStart.isValid()) {
                    return;
                }
            }

            //此处加一行判断即可，其他全是抄原版的
            if ((structurePlacement instanceof PositionPlacement positionPlacement && positionPlacement.isDOTEPlacementChunk(this, pos.x, pos.z)) || structurePlacement.isStructureChunk(pStructureState, pos.x, pos.z)) {
                if (iterator.size() == 1) {
                    this.tryGenerateStructure(iterator.get(0), pStructureManager, pRegistryAccess, randomState, pStructureTemplateManager, pStructureState.getLevelSeed(), pChunk, pos, sectionPos);
                } else {
                    ArrayList<StructureSet.StructureSelectionEntry> list = new ArrayList<>(iterator.size());
                    list.addAll(iterator);
                    WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
                    worldgenRandom.setLargeFeatureSeed(pStructureState.getLevelSeed(), pos.x, pos.z);
                    int i = 0;

                    StructureSet.StructureSelectionEntry $$16;
                    for (Iterator<StructureSet.StructureSelectionEntry> var15 = list.iterator(); var15.hasNext(); i += $$16.weight()) {
                        $$16 = var15.next();
                    }

                    while (!list.isEmpty()) {
                        int $$17 = worldgenRandom.nextInt(i);
                        int $$18 = 0;

                        for (Iterator<StructureSet.StructureSelectionEntry> var17 = list.iterator(); var17.hasNext(); ++$$18) {
                            StructureSet.StructureSelectionEntry $$19 = var17.next();
                            $$17 -= $$19.weight();
                            if ($$17 < 0) {
                                break;
                            }
                        }

                        StructureSet.StructureSelectionEntry selectionEntry = list.get($$18);
                        if (this.tryGenerateOtherStructure(selectionEntry, pStructureManager, pRegistryAccess, randomState, pStructureTemplateManager, pStructureState.getLevelSeed(), pChunk, pos, sectionPos)) {
                            return;
                        }
                        list.remove($$18);
                        i -= selectionEntry.weight();
                    }

                }
            }
        });
    }

    private boolean tryGenerateStructure(StructureSet.StructureSelectionEntry pStructureSelectionEntry, StructureManager pStructureManager, RegistryAccess pRegistryAccess, RandomState pRandom, StructureTemplateManager pStructureTemplateManager, long pSeed, ChunkAccess pChunk, ChunkPos pChunkPos, SectionPos pSectionPos) {
        Structure $$9 = pStructureSelectionEntry.structure().value();
        int $$10 = fetchReferences(pStructureManager, pChunk, pSectionPos, $$9);
        HolderSet<Biome> $$11 = $$9.biomes();
        Objects.requireNonNull($$11);
        Predicate<Holder<Biome>> $$12 = $$11::contains;
        StructureStart $$13 = $$9.generate(pRegistryAccess, this, this.biomeSource, pRandom, pStructureTemplateManager, pSeed, pChunkPos, $$10, pChunk, $$12);
        if ($$13.isValid()) {
            pStructureManager.setStartForStructure(pSectionPos, $$9, $$13, pChunk);
            return true;
        }
        return false;
    }

    /**
     * 检查不能和地标过近
     */
    private boolean tryGenerateOtherStructure(StructureSet.StructureSelectionEntry pStructureSelectionEntry, StructureManager pStructureManager, RegistryAccess pRegistryAccess, RandomState pRandom, StructureTemplateManager pStructureTemplateManager, long pSeed, ChunkAccess pChunk, ChunkPos pChunkPos, SectionPos pSectionPos) {
        if (getBiomeSource() instanceof SMCBiomeProvider) {
            for (SMCStructurePoses structure : SMCStructurePoses.values()) {
                Vec3i p = structure.getPos();
                int chunkX = p.getX() >> 4;
                int chunkZ = p.getZ() >> 4;

                if (Math.pow(chunkX - pChunkPos.x, 2) + Math.pow(chunkZ - pChunkPos.z, 2) < Math.pow(SMCConfig.MIN_CHUNK_BETWEEN_STRUCTURE.get(), 2)) {
                    return false;
                }
            }
        }
        return this.tryGenerateStructure(pStructureSelectionEntry, pStructureManager, pRegistryAccess, pRandom, pStructureTemplateManager, pSeed, pChunk, pChunkPos, pSectionPos);
    }

    private static int fetchReferences(StructureManager pStructureManager, ChunkAccess pChunk, SectionPos pSectionPos, Structure pStructure) {
        StructureStart $$4 = pStructureManager.getStartForStructure(pSectionPos, pStructure, pChunk);
        return $$4 != null ? $$4.getReferences() : 0;
    }

    /**
     * 搜索结构必备，不能少
     */
    @Nullable
    @Override
    public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel level, @NotNull HolderSet<Structure> targetStructures, @NotNull BlockPos pos, int searchRadius, boolean skipKnownStructures) {
        ChunkGeneratorStructureState state = level.getChunkSource().getGeneratorState();

        @Nullable
        Pair<BlockPos, Holder<Structure>> nearest = super.findNearestMapStructure(level, targetStructures, pos, searchRadius, skipKnownStructures);

        Map<PositionPlacement, Set<Holder<Structure>>> placementSetMap = new Object2ObjectArrayMap<>();
        for (Holder<Structure> holder : targetStructures) {
            for (StructurePlacement structureplacement : state.getPlacementsForStructure(holder)) {
                if (structureplacement instanceof PositionPlacement landmarkPlacement) {
                    placementSetMap.computeIfAbsent(landmarkPlacement, v -> new ObjectArraySet<>()).add(holder);
                }
            }
        }
        if (placementSetMap.isEmpty()) return nearest;
        if (this.getBiomeSource() instanceof SMCBiomeProvider) {
            for (Map.Entry<PositionPlacement, Set<Holder<Structure>>> landmarkPlacement : placementSetMap.entrySet()) {
                PositionPlacement placement = landmarkPlacement.getKey();
                Vec3i p = Vec3i.ZERO;
                //在这里判断结构是什么，并且返回对应的点
                for (SMCStructurePoses structure : SMCStructurePoses.values()) {
                    if (structure == SMCStructurePoses.valueOf(placement.structure)) {
                        p = structure.getPos();
                        break;
                    }
                }

                BlockPos pos1 = new BlockPos(p.getX(), p.getY(), p.getZ());
                for (Holder<Structure> targetStructure : targetStructures) {
                    if (landmarkPlacement.getValue().contains(targetStructure)) {
                        nearest = new Pair<>(pos1, targetStructure);
                    }
                }
            }
        }

        return nearest;
    }

}
