package io.github.fishstiz.softloader.config;

import io.github.fishstiz.softloader.SoftLoader;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Config {
    @SuppressWarnings("FieldCanBeLocal")
    private final String enabled = "datapacks";
    @SuppressWarnings("FieldCanBeLocal")
    private final String disabled = "";
    private final List<String> loadOrder = Collections.emptyList();

    public Optional<Path> getEnabled() {
        return toPath(this.enabled);
    }

    public Optional<Path> getDisabled() {
        return toPath(this.disabled);
    }

    public List<String> getLoadOrder() {
        return this.loadOrder;
    }

    private static Optional<Path> toPath(String path) {
        if (path == null || path.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(Paths.get(path));
        } catch (InvalidPathException e) {
            SoftLoader.LOG.error("[soft_loader] Not a valid path: {}", path);
            return Optional.empty();
        }
    }
}
