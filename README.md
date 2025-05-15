# ModLoaderDetector

ModLoaderDetector is a Java library designed to parse Minecraft's version manifest JSON files (typically located at `.minecraft/versions/x.yy.z/x.yy.z.json`) 
to identify the mod loader used, along with its version and the Minecraft version it supports.

## Features

Parses the manifest JSON file into an object that lets you easily retrieve the following values:
  - Loader
  - Loader version
  - Game version
  - Game version type (e.g. release, snapshot, etc.)

## Usage

### Gradle Dependency

```gradle
repositories {
    maven {
        name "radRepo"
        url "https://maven.radsteve.net/public"
    }
}

dependencies {
    implementation("me.andreasmelone:modloader-detector:1.0.0")
}
```

### Example Usage

```java
public class Main {
    public static void main(String[] args) {
        File jsonFile = new File(".minecraft/versions/1.16.5/1.16.5.json");
        try {
            ModLoaderData data = ModLoader.findModLoader(jsonFile)
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported mod loader"));

            System.out.println("Mod Loader: " + data.getLoader());
            System.out.println("Loader Version: " + data.getLoaderVersion());
            System.out.println("Minecraft Version: " + data.getMinecraftVersion());
            System.out.println("Minecraft Version Type: " + data.getMinecraftVersionType());
        } catch(JsonSyntaxException e) {
            System.out.println("Failed to parse the json!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## Supported Mod Loaders

- Forge (1.13+)
- NeoForge 
- Fabric
- Quilt
- Legacy Forge (1.12.2 and below)

## License

This project is licensed under the MIT License. See the LICENSE file for details.