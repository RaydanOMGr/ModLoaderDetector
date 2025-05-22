package me.andreasmelone.modloaderdetector.versionjson;

import java.util.List;

// This class is here just so I don't need to manually traverse and type check the JsonObject, which is a pain in the ass
// it only contains stuff that is actually needed
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
        private List<Argument> game;

        public List<Argument> getGame() {
            return game;
        }
    }

    public static class Library {
        private String name;

        public String getName() {
            return name;
        }
    }
}
