package io.github.fishstiz.softloader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.fishstiz.softloader.config.Config;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.DataPackConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class SoftLoader {
    public static final String MOD_ID = "soft_loader";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
    public static final Config CONFIG = loadConfig();

    private static Config loadConfig() {
        final Path path = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Config config;

        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                config = gson.fromJson(reader, Config.class);
            } catch (IOException e) {
                LOG.error("Failed to load config: {}. {}", path, e);
                return new Config();
            }
        } else {
            config = new Config();
            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(config, writer);
            } catch (IOException e) {
                LOG.error("Failed to save default config: {}. {}", path, e);
            }
        }

        return config;
    }

    public static DataPackConfig withLoadOrder(DataPackConfig dataPackConfig) {
        Set<String> enabled = new ObjectLinkedOpenHashSet<>(dataPackConfig.getEnabled());
        Set<String> disabled = new ObjectLinkedOpenHashSet<>(dataPackConfig.getDisabled());

        for (String packId : SoftLoader.CONFIG.getLoadOrder()) {
            disabled.remove(packId);
            enabled.remove(packId);
            enabled.add(packId);
        }

        return new DataPackConfig(List.copyOf(enabled), List.copyOf(disabled));
    }
}
