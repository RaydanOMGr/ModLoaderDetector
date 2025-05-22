package me.andreasmelone.modloaderdetector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ModLoaderData {
    @NotNull private final String minecraftVersion;
    @NotNull private final VersionType minecraftVersionType;
    @Nullable private final String loaderVersion;
    @NotNull private final ModLoader loader;

    public ModLoaderData(@NotNull String minecraftVersion,
                         @NotNull VersionType minecraftVersionType,
                         @Nullable String loaderVersion, @NotNull ModLoader loader) {
        this.minecraftVersion = minecraftVersion;
        this.minecraftVersionType = minecraftVersionType;
        this.loaderVersion = loaderVersion;
        this.loader = loader;
    }

    @NotNull
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    @NotNull
    public VersionType getMinecraftVersionType() {
        return minecraftVersionType;
    }

    @Nullable
    public String getLoaderVersion() {
        return loaderVersion;
    }

    @NotNull
    public ModLoader getLoader() {
        return loader;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ModLoaderData that = (ModLoaderData) o;
        return Objects.equals(minecraftVersion, that.minecraftVersion)
                && minecraftVersionType == that.minecraftVersionType
                && Objects.equals(loaderVersion, that.loaderVersion)
                && loader == that.loader;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minecraftVersion, minecraftVersionType, loaderVersion, loader);
    }

    @Override
    public String toString() {
        return "ModLoaderData{" +
                "minecraftVersion='" + minecraftVersion + '\'' +
                ", minecraftVersionType=" + minecraftVersionType +
                ", loaderVersion='" + loaderVersion + '\'' +
                ", loader=" + loader +
                '}';
    }
}
