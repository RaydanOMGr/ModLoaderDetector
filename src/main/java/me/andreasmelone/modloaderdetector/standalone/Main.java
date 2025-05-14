package me.andreasmelone.modloaderdetector.standalone;

import com.google.gson.JsonSyntaxException;
import me.andreasmelone.modloaderdetector.ModLoader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        File[] currentFiles = new File(".").listFiles();
        assert currentFiles != null;
        for(File file : currentFiles) {
            if (!file.getName().endsWith(".json") || !file.isFile()) return;

            try {
                Optional<ModLoader> modLoader = ModLoader.findModLoader(file);
                if(modLoader.isPresent()) {
                    System.out.println("Detected " + modLoader.get().name() + " in " + file.getName() + "!");
                } else {
                    System.out.println(file.getName() + " contains no known loader.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JsonSyntaxException e) {
                System.out.println(file.getName() + " is an invalid json and the loader cannot be identified.");
            }
        }
    }
}