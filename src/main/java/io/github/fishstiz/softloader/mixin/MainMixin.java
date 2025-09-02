package io.github.fishstiz.softloader.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.fishstiz.softloader.SoftLoader;
import net.minecraft.server.Main;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.DataPackConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Main.class)
public class MainMixin {
    @WrapOperation(method = "loadOrCreateConfig", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/dedicated/DedicatedServerProperties;initialDataPackConfiguration:Lnet/minecraft/world/level/DataPackConfig;"
    ))
    private static DataPackConfig addLoadOrderOnInit(DedicatedServerProperties instance, Operation<DataPackConfig> original) {
        return SoftLoader.withLoadOrder(original.call(instance));
    }
}
