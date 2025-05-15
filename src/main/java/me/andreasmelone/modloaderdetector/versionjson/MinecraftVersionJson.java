package me.andreasmelone.modloaderdetector.versionjson;

import java.util.List;

// This class is here just so I don't need to manually traverse and type check the JsonObject, which is a pain in the ass
public class MinecraftVersionJson {
    private String id;
    private String inheritsFrom;
    private String type;
    private String mainClass;
    private Arguments arguments;
    private List<Library> libraries;

    public String getId() {
        return id;
    }

    public String getInheritsFrom() {
        return inheritsFrom;
    }

    public String getType() {
        return type;
    }

    public String getMainClass() {
        return mainClass;
    }

    public Arguments getArguments() {
        return arguments;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public static class Arguments {
        private List<String> game;
        private List<String> jvm;

        public List<String> getGame() {
            return game;
        }

        public List<String> getJvm() {
            return jvm;
        }
    }

    public static class Library {
        private String name;

        public String getName() {
            return name;
        }
    }
}
