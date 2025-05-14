package me.andreasmelone.modloaderdetector;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static me.andreasmelone.modloaderdetector.util.Util.*;

/**
 * Represents supported Minecraft mod loaders and allows detection based on launch JSON metadata.
 */
public enum ModLoader {
    /**
     * Forge mod loader.
     */
    FORGE {
        @Override
        protected boolean isModLoader(JsonObject json) {
            return hasLaunchTarget(json,
                    "cpw.mods.bootstraplauncher.BootstrapLauncher",
                    "forgeclient");
        }
    },

    /**
     * NeoForge mod loader.
     */
    NEOFORGE {
        @Override
        protected boolean isModLoader(JsonObject json) {
            return hasLaunchTarget(json,
                    "cpw.mods.bootstraplauncher.BootstrapLauncher",
                    "neoforgeclient");
        }
    },

    /**
     * Fabric mod loader.
     */
    FABRIC {
        @Override
        protected boolean isModLoader(JsonObject json) {
            final String expectedMain = "net.fabricmc.loader.impl.launch.knot.KnotClient";
            final String actualMain = getValidString(json, "mainClass");
            return expectedMain.equals(actualMain);
        }
    },

    /**
     * Quilt mod loader.
     */
    QUILT {
        @Override
        protected boolean isModLoader(JsonObject json) {
            final String expectedMain = "org.quiltmc.loader.impl.launch.knot.KnotClient";
            final String actualMain = getValidString(json, "mainClass");
            return expectedMain.equals(actualMain);
        }
    },

    /**
     * Forge mod loader pre-1.13.
     * This may be slightly inaccurate, as some other modloaders or clients (e.g. Liteloader) may define launchwrapper as their main class too.
     */
    LEGACY_FORGE {
        @Override
        protected boolean isModLoader(JsonObject json) {
            final String expectedMain = "net.minecraft.launchwrapper.Launch";
            final String actualMain = getValidString(json, "mainClass");
            return expectedMain.equals(actualMain);
        }
    };

    /**
     * Determines whether the given JSON object matches this mod loader.
     *
     * @param json the launch JSON to inspect
     * @return true if the loader matches, false otherwise
     */
    protected abstract boolean isModLoader(JsonObject json);

    /**
     * Attempts to identify the mod loader from the given JSON object.
     *
     * @param json the parsed JSON object
     * @return an optional containing the identified mod loader, or empty if none matched
     */
    public static Optional<ModLoader> findModLoader(JsonObject json) {
        for (ModLoader loader : values()) {
            if (loader.isModLoader(json)) {
                return Optional.of(loader);
            }
        }
        return Optional.empty();
    }

    /**
     * Attempts to identify the mod loader from a JSON string.
     *
     * @param jsonString a JSON-formatted string representing launch metadata
     * @return an optional containing the identified mod loader, or empty if none matched
     * @throws JsonSyntaxException if the json is invalid
     */
    public static Optional<ModLoader> findModLoader(String jsonString) throws JsonSyntaxException {
        JsonObject parsed = GSON.fromJson(jsonString, JsonObject.class);
        return findModLoader(parsed);
    }

    /**
     * Attempts to identify the mod loader from a JSON file path.
     *
     * @param pathToJson the path to the JSON file
     * @return an optional containing the identified mod loader, or empty if none matched
     * @throws IOException if the file could not be read
     * @throws JsonSyntaxException if the file contains invalid json
     */
    public static Optional<ModLoader> findModLoader(Path pathToJson) throws IOException, JsonSyntaxException {
        String content = Files.readString(pathToJson, StandardCharsets.UTF_8);
        return findModLoader(content);
    }

    /**
     * Attempts to identify the mod loader from a JSON file.
     *
     * @param jsonFile the JSON file
     * @return an optional containing the identified mod loader, or empty if none matched
     * @throws IOException if the file could not be read
     * @throws JsonSyntaxException if the file contains invalid json
     */
    public static Optional<ModLoader> findModLoader(File jsonFile) throws IOException, JsonSyntaxException {
        return findModLoader(jsonFile.toPath());
    }
}
