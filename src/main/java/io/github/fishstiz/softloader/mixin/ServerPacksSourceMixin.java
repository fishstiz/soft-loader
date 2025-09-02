package io.github.fishstiz.softloader.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.fishstiz.softloader.repository.SoftLoaderRepositorySource;
import net.minecraft.server.packs.repository.*;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ServerPacksSource.class)
public abstract class ServerPacksSourceMixin {
    @WrapOperation(method = "createPackRepository(Ljava/nio/file/Path;Lnet/minecraft/world/level/validation/DirectoryValidator;)Lnet/minecraft/server/packs/repository/PackRepository;", at = @At(
            value = "NEW",
            target = "([Lnet/minecraft/server/packs/repository/RepositorySource;)Lnet/minecraft/server/packs/repository/PackRepository;"
    ))
    private static PackRepository addRepositorySources(
            RepositorySource[] sources,
            Operation<PackRepository> original,
            @Local(argsOnly = true) DirectoryValidator validator
    ) {
        List<SoftLoaderRepositorySource> extraSources = SoftLoaderRepositorySource.getSources(validator);
        if (!extraSources.isEmpty()) {
            return original.call((Object) ArrayUtils.addAll(sources, extraSources.toArray(new RepositorySource[0])));
        } else {
            return original.call((Object) sources);
        }
    }
}