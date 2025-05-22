package me.andreasmelone.modloaderdetector;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class VersionType {
    private static final Map<String, VersionType> knownTypes = new HashMap<>();

    public static final VersionType RELEASE = register("release");
    public static final VersionType SNAPSHOT = register("snapshot");
    public static final VersionType OLD_BETA = register("old_beta");
    public static final VersionType OLD_ALPHA = register("old_alpha");

    private final String name;
    private final boolean known;

    private VersionType(String name, boolean known) {
        this.name = name;
        this.known = known;
    }

    /**
     * Creates and registers a version type. By default, all registered types are known. Unknown types must not be registered.
     *
     * @param name the name of this type, capitalization does not matter
     * @return The created and registered version type.
     */
    private static VersionType register(String name) {
        VersionType type = new VersionType(name, true);
        knownTypes.put(name.toLowerCase(Locale.ROOT), type);
        return type;
    }

    /**
     * Gets the version type from a string containing the version type name.
     *
     * @param name the name of the version type
     * @return the VersionType object
     */
    public static VersionType from(String name) {
        return knownTypes.getOrDefault(
                name.toLowerCase(Locale.ROOT),
                new VersionType(name.toLowerCase(Locale.ROOT), false)
        );
    }

    /**
     * All known types are featured on piston meta.<p>
     * Following version types are considered known:
     * <ul>
     *     <li>release</li>
     *     <li>snapshot</li>
     *     <li>old_beta</li>
     *     <li>old_alpha</li>
     * </ul>
     * Their predefined types can be accessed statically. Capitalization is ignored.
     *
     * @return whether the version type is known or not
     */
    public boolean isKnown() {
        return known;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VersionType that = (VersionType) o;
        return known == that.known && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, known);
    }

    @Override
    public String toString() {
        return name;
    }
}
