package me.andreasmelone.modloaderdetector;

import java.util.Objects;

public class ModLoaderData {
    private final String minecraftVersion;
    private final VersionType minecraftVersionType;
    private final String loaderVersion;
    private final ModLoader loader;

    public ModLoaderData(String minecraftVersion, VersionType minecraftVersionType, String loaderVersion, ModLoader loader) {
        this.minecraftVersion = minecraftVersion;
        this.minecraftVersionType = minecraftVersionType;
        this.loaderVersion = loaderVersion;
        this.loader = loader;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public VersionType getMinecraftVersionType() {
        return minecraftVersionType;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }

    public ModLoader getLoader() {
        return loader;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ModLoaderData that = (ModLoaderData) o;
        return Objects.equals(minecraftVersion, that.minecraftVersion) && minecraftVersionType == that.minecraftVersionType && Objects.equals(loaderVersion, that.loaderVersion) && loader == that.loader;
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
