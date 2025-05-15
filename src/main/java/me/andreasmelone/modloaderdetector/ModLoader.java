package me.andreasmelone.modloaderdetector;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.andreasmelone.modloaderdetector.util.MavenDependency;
import me.andreasmelone.modloaderdetector.versionjson.MinecraftVersionJson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
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
        protected Optional<ModLoaderData> getModLoaderData(JsonObject json) {
            if(!hasLaunchTarget(json,
                    "cpw.mods.bootstraplauncher.BootstrapLauncher",
                    "forgeclient")) return Optional.empty();
            MinecraftVersionJson version = GSON.fromJson(json, MinecraftVersionJson.class);
            String neoVersion = getArgument(version.getArguments().getGame().toArray(new String[0]), "--fml.forgeVersion");
            return Optional.of(new ModLoaderData(
                    version.getInheritsFrom(),
                    VersionType.from(version.getType()),
                    neoVersion,
                    this
            ));
        }
    },

    /**
     * NeoForge mod loader.
     */
    NEOFORGE {
        @Override
        protected Optional<ModLoaderData> getModLoaderData(JsonObject json) {
            if(!hasLaunchTarget(json,
                    "cpw.mods.bootstraplauncher.BootstrapLauncher",
                    "neoforgeclient")) return Optional.empty();
            MinecraftVersionJson version = GSON.fromJson(json, MinecraftVersionJson.class);
            String neoVersion = getArgument(version.getArguments().getGame().toArray(new String[0]), "--fml.neoForgeVersion");
            return Optional.of(new ModLoaderData(
                    version.getInheritsFrom(),
                    VersionType.from(version.getType()),
                    neoVersion,
                    this
            ));
        }
    },

    /**
     * Fabric mod loader.
     */
    FABRIC {
        @Override
        protected Optional<ModLoaderData> getModLoaderData(JsonObject json) {
            final String fabricGroup = "net.fabricmc";
            final String fabricArtifact = "fabric-loader";
            final String expectedMain = "net.fabricmc.loader.impl.launch.knot.KnotClient";

            MinecraftVersionJson version = GSON.fromJson(json, MinecraftVersionJson.class);
            if(!expectedMain.equals(version.getMainClass())) return Optional.empty();

            // generally, I prefer to avoid streams because they look convoluted and unreadable
            // but here this seems to be the compactest solution, as I do not want three more levels of indentation
            // it just parses all libraries from version into MavenDependencies and finds the first one that
            // matches the forge group and artifact id
            Optional<MavenDependency> fabricDep = version.getLibraries().stream()
                    .map(library -> MavenDependency.parse(library.getName()))
                    .filter(Objects::nonNull)
                    .filter(dep -> dep.getGroup().equalsIgnoreCase(fabricGroup)
                            && dep.getArtifactId().equalsIgnoreCase(fabricArtifact))
                    .findFirst();

            return fabricDep.map(mavenDependency -> new ModLoaderData(
                    version.getInheritsFrom(),
                    VersionType.from(version.getType()),
                    mavenDependency.getVersion(),
                    this
            ));
        }
    },

    /**
     * Quilt mod loader.
     */
    QUILT {
        @Override
        protected Optional<ModLoaderData> getModLoaderData(JsonObject json) {
            final String quiltGroup = "org.quiltmc";
            final String quiltArtifact = "quilt-loader";
            final String expectedMain = "org.quiltmc.loader.impl.launch.knot.KnotClient";

            MinecraftVersionJson version = GSON.fromJson(json, MinecraftVersionJson.class);
            if(!expectedMain.equals(version.getMainClass())) return Optional.empty();

            Optional<MavenDependency> quiltDep = version.getLibraries().stream()
                    .map(library -> MavenDependency.parse(library.getName()))
                    .filter(Objects::nonNull)
                    .filter(dep -> dep.getGroup().equalsIgnoreCase(quiltGroup)
                            && dep.getArtifactId().equalsIgnoreCase(quiltArtifact))
                    .findFirst();

            return quiltDep.map(mavenDependency -> new ModLoaderData(
                    version.getInheritsFrom(),
                    VersionType.from(version.getType()),
                    mavenDependency.getVersion(),
                    this
            ));
        }
    },

    /**
     * Forge mod loader pre-1.13.
     */
    LEGACY_FORGE {
        @Override
        protected Optional<ModLoaderData> getModLoaderData(JsonObject json) {
            final String forgeGroup = "net.minecraftforge";
            final String forgeArtifact = "forge";
            final String expectedMain = "net.minecraft.launchwrapper.Launch";

            final String actualMain = getValidString(json, "mainClass");
            if(!expectedMain.equals(actualMain)) return Optional.empty();

            MinecraftVersionJson version = GSON.fromJson(json, MinecraftVersionJson.class);

            Optional<MavenDependency> forgeDep = version.getLibraries().stream()
                    .map(library -> MavenDependency.parse(library.getName()))
                    .filter(Objects::nonNull)
                    .filter(dep -> dep.getGroup().equalsIgnoreCase(forgeGroup)
                            && dep.getArtifactId().equalsIgnoreCase(forgeArtifact))
                    .findFirst();

            return forgeDep.map(mavenDependency -> {
                // this is a failsafe, in case the inheritsFrom isn't defined
                // legacy forge always used to include the minecraft version in its loader version
                // e.g. 1.12.2-14.23.5.2860
                String[] splitVersion = mavenDependency.getVersion().split("-", 2);
                String versionStr = version.getInheritsFrom() == null ?
                        splitVersion[0] : version.getInheritsFrom();

                return new ModLoaderData(
                        versionStr,
                        VersionType.from(version.getType()),
                        mavenDependency.getVersion(),
                        this
                );
            });
        }
    };

    /**
     * Determines whether the given JSON object matches this mod loader.
     *
     * @param json the launch JSON to inspect
     * @return an optional with the modloader data if the loader is found or empty if no loader is found
     */
    protected abstract Optional<ModLoaderData> getModLoaderData(JsonObject json);

    /**
     * Attempts to identify the mod loader from the given JSON object.
     *
     * @param json the parsed JSON object
     * @return an optional with the modloader data if the loader is found or empty if no loader is found
     */
    public static Optional<ModLoaderData> findModLoader(JsonObject json) {
        for (ModLoader loader : values()) {
            Optional<ModLoaderData> data = loader.getModLoaderData(json);
            if (data.isPresent()) {
                return data;
            }
        }
        return Optional.empty();
    }

    /**
     * Attempts to identify the mod loader from a JSON string.
     *
     * @param jsonString a JSON-formatted string representing launch metadata
     * @return an optional with the modloader data if the loader is found or empty if no loader is found
     * @throws JsonSyntaxException if the json is invalid
     */
    public static Optional<ModLoaderData> findModLoader(String jsonString) throws JsonSyntaxException {
        JsonObject parsed = GSON.fromJson(jsonString, JsonObject.class);
        return findModLoader(parsed);
    }

    /**
     * Attempts to identify the mod loader from a JSON file path.
     *
     * @param pathToJson the path to the JSON file
     * @return an optional with the modloader data if the loader is found or empty if no loader is found
     * @throws IOException if the file could not be read
     * @throws JsonSyntaxException if the file contains invalid json
     */
    public static Optional<ModLoaderData> findModLoader(Path pathToJson) throws IOException, JsonSyntaxException {
        String content = new String(Files.readAllBytes(pathToJson), StandardCharsets.UTF_8);
        return findModLoader(content);
    }

    /**
     * Attempts to identify the mod loader from a JSON file.
     *
     * @param jsonFile the JSON file
     * @return an optional with the modloader data if the loader is found or empty if no loader is found
     * @throws IOException if the file could not be read
     * @throws JsonSyntaxException if the file contains invalid json
     */
    public static Optional<ModLoaderData> findModLoader(File jsonFile) throws IOException, JsonSyntaxException {
        return findModLoader(jsonFile.toPath());
    }
}
