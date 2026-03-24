package io.github.fishstiz.softloader.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.fishstiz.softloader.SoftLoader;
import io.github.fishstiz.softloader.repository.SoftLoaderRepositorySource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.WorldDataConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.LinkedHashSet;
import java.util.Set;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @ModifyArg(method = "openCreateWorldScreen", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;createDefaultLoadConfig(Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/world/level/WorldDataConfiguration;)Lnet/minecraft/server/WorldLoader$InitConfig;"
    ))
    private static PackRepository addRepositorySourcesOnInit(PackRepository packRepository, @Local(argsOnly = true) Minecraft minecraft) {
        // Fabric API already changes sources to a LinkedHashSet in ResourcePackManagerMixin#construct, but just in case
        Set<RepositorySource> sources = new LinkedHashSet<>(packRepository.sources);
        sources.addAll(SoftLoaderRepositorySource.getSources(minecraft.directoryValidator()));
        packRepository.sources = sources;
        return packRepository;
    }

    @ModifyArg(method = "openCreateWorldScreen", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;createDefaultLoadConfig(Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/world/level/WorldDataConfiguration;)Lnet/minecraft/server/WorldLoader$InitConfig;"
    ))
    private static WorldDataConfiguration addLoadOrderOnInit(WorldDataConfiguration worldDataConfiguration) {
        return new WorldDataConfiguration(SoftLoader.withLoadOrder(worldDataConfiguration.dataPacks()), worldDataConfiguration.enabledFeatures());
    }
}
