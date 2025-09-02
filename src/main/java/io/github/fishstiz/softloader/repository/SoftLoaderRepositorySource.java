package io.github.fishstiz.softloader.repository;

import io.github.fishstiz.softloader.SoftLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.validation.DirectoryValidator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SoftLoaderRepositorySource extends FolderRepositorySource {
    public static final PackSource PACK_SOURCE_ENABLED = createPackSource(true);
    public static final PackSource PACK_SOURCE_DISABLED = createPackSource(false);

    public SoftLoaderRepositorySource(Path path, PackSource packSource, DirectoryValidator directoryValidator) {
        super(path, PackType.SERVER_DATA, packSource, directoryValidator);
    }

    private static PackSource createPackSource(boolean shouldEnableAutomatically) {
        return PackSource.create(
                description -> Component.translatable("pack.nameAndSource", description, SoftLoader.MOD_ID),
                shouldEnableAutomatically
        );
    }

    public static List<SoftLoaderRepositorySource> getSources(DirectoryValidator directoryValidator) {
        List<SoftLoaderRepositorySource> sources = new ArrayList<>();

        SoftLoader.CONFIG.getDisabled()
                .ifPresent(path -> sources.add(new SoftLoaderRepositorySource(path, PACK_SOURCE_DISABLED, directoryValidator)));

        SoftLoader.CONFIG.getEnabled()
                .ifPresent(path -> sources.add(new SoftLoaderRepositorySource(path, PACK_SOURCE_ENABLED, directoryValidator)));

        return sources;
    }
}
