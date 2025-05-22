package me.andreasmelone.modloaderdetector.versionjson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Argument {
    private List<String> values;

    public Argument() {}

    public Argument(String value) {
        this.values = Collections.singletonList(value);
    }

    public Argument(List<String> values) {
        this.values = new ArrayList<>(values);
    }

    public List<String> getValues() {
        return values;
    }

    public static class Serializer implements JsonDeserializer<Argument> {
        @Override
        public Argument deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<String> values = new ArrayList<>();

            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                values.add(json.getAsString());
            } else if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                JsonElement valuesElement = obj.get("values");
                if (valuesElement != null && valuesElement.isJsonArray()) {
                    for (JsonElement el : valuesElement.getAsJsonArray()) {
                        values.add(el.getAsString());
                    }
                } else {
                    return new Argument();
                }
            } else {
                return new Argument();
            }

            return new Argument(values);
        }
    }
}
