package me.andreasmelone.modloaderdetector.standalone;

import com.google.gson.JsonSyntaxException;
import me.andreasmelone.modloaderdetector.ModLoader;
import me.andreasmelone.modloaderdetector.ModLoaderData;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        File[] currentFiles = new File(".").listFiles();
        assert currentFiles != null;
        for(File file : currentFiles) {
            if (!file.getName().endsWith(".json") || !file.isFile()) continue;

            try {
                Optional<ModLoaderData> modLoader = ModLoader.findModLoader(file);
                if(modLoader.isPresent()) {
                    ModLoaderData data = modLoader.get();
                    String knownText = " (unknown)";
                    if(data.getMinecraftVersionType() != null && data.getMinecraftVersionType().isKnown()) knownText = "";

                    System.out.println(file.getName() + ":");
                    System.out.println("\tMinecraft version: " + data.getMinecraftVersion());
                    System.out.println("\tMinecraft version type" + knownText + ": " + data.getMinecraftVersionType());
                    System.out.println("\tLoader: " + data.getLoader());
                    System.out.println("\tLoader version: " + data.getLoaderVersion());
                } else {
                    System.out.println(file.getName() + " contains no data to identify the loader or version.");
                }
            } catch (JsonSyntaxException e) {
                System.out.println(file.getName() + " is an invalid json and the loader cannot be identified.");
            } catch (IOException e) {
                // what a party-killer
                System.out.println(file.getName() + " cannot be loaded.");
                e.printStackTrace();
            }
            System.out.println();
        }
    }
}