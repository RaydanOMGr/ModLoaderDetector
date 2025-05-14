package me.andreasmelone.modloaderdetector.util;

import com.google.gson.*;

import java.util.Optional;
import java.util.function.Predicate;

public class Util {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Finds a value by key in a {@link JsonObject}.
     *
     * @param jsonObject The {@link JsonObject} in which to search for the value.
     * @param key        The key for which the value needs to be found.
     * @return An {@link Optional} containing the {@link JsonElement} if it exists,
     * or an empty {@link Optional} if the value was not found.
     */
    public static Optional<JsonElement> findValidValue(JsonObject jsonObject, String key) {
        return findValidValue(jsonObject, key, (el) -> true);
    }

    /**
     * Finds a value by key in a {@link JsonObject} and checks it against a predicate.
     *
     * @param jsonObject The {@link JsonObject} in which to search for the value.
     * @param key        The key for which the value needs to be found.
     * @param predicate  The predicate that the value of the key must satisfy.
     * @return An {@link Optional} containing the {@link JsonElement} if it exists and matches the predicate,
     * or an empty {@link Optional} if the value was not found or did not match.
     */
    public static Optional<JsonElement> findValidValue(JsonObject jsonObject, String key, Predicate<JsonElement> predicate) {
        if (!jsonObject.has(key)) {
            return Optional.empty();
        }

        JsonElement element = jsonObject.get(key);
        return predicate.test(element) ? Optional.of(element) : Optional.empty();
    }

    /**
     * Helper method to fetch a valid string value from a {@link JsonObject}
     * by key, ensuring it matches the specified predicate.
     *
     * @param obj       The {@link JsonObject} from which to retrieve the value.
     * @param key       The key for the value to be retrieved.
     * @param predicate The predicate that the value must satisfy.
     * @return The string value if found and valid, or {@code null} if not found
     * or invalid.
     */
    public static String getValidString(JsonObject obj, String key, Predicate<JsonElement> predicate) {
        return findValidValue(obj, key, predicate)
                .map(JsonElement::getAsString)
                .orElse(null);
    }

    /**
     * Helper method to fetch a valid string value from a {@link JsonObject}
     * by key.
     *
     * @param obj       The {@link JsonObject} from which to retrieve the value.
     * @param key       The key for the value to be retrieved.
     * @return The string value if found and valid, or {@code null} if not found
     * or invalid.
     */
    public static String getValidString(JsonObject obj, String key) {
        return findValidValue(obj, key, (element) -> true)
                .map(JsonElement::getAsString)
                .orElse(null);
    }

    /**
     * Checks if the given JSON object represents a mod loader with a specific main class
     * and launch target argument.
     *
     * <p>This method is used to detect loaders like Forge or NeoForge that share the same
     * main class but differ by the {@code --launchTarget} argument.</p>
     *
     * @param json the JSON object representing the launch metadata
     * @param expectedMainClass the fully qualified name of the expected main class
     * @param expectedLaunchTarget the expected value of the {@code --launchTarget} argument (e.g. {@code "forgeclient"})
     * @return {@code true} if the JSON matches the expected main class and launch target, {@code false} otherwise
     */
    public static boolean hasLaunchTarget(JsonObject json, String expectedMainClass, String expectedLaunchTarget) {
        final String mainClass = getValidString(json, "mainClass");
        if (!expectedMainClass.equals(mainClass)) return false;

        Optional<JsonElement> argsObj = findValidValue(json, "arguments", JsonElement::isJsonObject);
        if (argsObj.isEmpty()) return false;

        Optional<JsonElement> gameArgs = findValidValue(argsObj.get().getAsJsonObject(), "game", JsonElement::isJsonArray);
        if (gameArgs.isEmpty()) return false;

        JsonArray args = gameArgs.get().getAsJsonArray();
        for (int i = 0; i < args.size() - 1; i++) {
            JsonElement current = args.get(i);
            JsonElement next = args.get(i + 1);

            if (current.isJsonPrimitive() && current.getAsJsonPrimitive().isString()
                    && current.getAsString().equals("--launchTarget")
                    && next.isJsonPrimitive() && next.getAsJsonPrimitive().isString()
                    && next.getAsString().equals(expectedLaunchTarget)) {
                return true;
            }
        }

        return false;
    }
}
