package me.andreasmelone.modloaderdetector.util;

import java.util.Objects;

/**
 * The maven dependency notation, e.g. {@code net.fabricmc:fabric-loader:0.16.0}<p>
 * This is technically incomplete as it does not care about scope: group:artifactId:version:scope (might be called differently, not sure)
 */
public class MavenDependency {
    private final String group;
    private final String artifactId;
    private final String version;

    public MavenDependency(String group, String artifactId, String version) {
        this.group = group;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    /**
     * Parses a string into a {@link MavenDependency}
     *
     * @param notation The maven dependency notation, e.g. {@code net.fabricmc:fabric-loader:0.16.0}
     * @return The parsed {@link MavenDependency} or null if parsing went wrong
     */
    public static MavenDependency parse(String notation) {
        String[] split = notation.split(":");
        if(split.length < 3) return null;
        return new MavenDependency(split[0], split[1], split[2]);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MavenDependency that = (MavenDependency) o;
        return Objects.equals(group, that.group) && Objects.equals(artifactId, that.artifactId) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, artifactId, version);
    }

    @Override
    public String toString() {
        return group + ":" + artifactId + ":" + version;
    }
}
