package me.syberiak.cmdr.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.syberiak.cmdr.CMDR;

public class Config extends ConfigWrapper {
    // Config object fields
    public AppConfig appConfig;
    public Bindings bindings;

    public Config() {
        appConfig = new AppConfig();
        bindings = new Bindings();
    }

    public String getLanguage() {
        return appConfig.lang;
    }

    public String getVanillaPath() {
        return bindings.vanilla.path;
    }

    public void setVanillaPath(Path path) {
        setVanillaPath(path.toString());
    }

    public void setVanillaPath(String path) {
        bindings.vanilla.path = path;
    }

    public String getPrismPath() {
        return bindings.prism.path;
    }

    public void setPrismPath(String path) {
        bindings.vanilla.path = path;
    }

    public String[] getPrismInstances() {
        return bindings.prism.instances;
    }

    // Container classes
    static class AppConfig {
        String lang;

        AppConfig() {
            lang = Locale.getDefault().toString();
        }
    }

    static class Bindings {
        VanillaBindings vanilla;
        PrismBindings prism;

        Bindings() {
            vanilla = new VanillaBindings();
            prism = new PrismBindings();
        }

        static class VanillaBindings {
            String path;

            VanillaBindings() {
                path = CMDR.DEFAULT_MINECRAFT_PATH;
            }
        }

        static class PrismBindings {
            String path;
            String[] instances;

            PrismBindings() {
                path = CMDR.DEFAULT_MINECRAFT_PATH;
                instances = CMDR.parsePrismInstances();
            }
        }
    }
}

class ConfigWrapper {
    private static Config instance;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Config getInstance() {
        if (instance == null) {
            instance = fromDefaults();
        }
        return instance;
    }

    public static void load(File file) {
        instance = fromFile(file);

        // no config file found
        if (instance == null) {
            instance = fromDefaults();
        }
    }

    public static void load(String file) {
        load(new File(file));
    }

    private static Config fromDefaults() {
        return new Config();
    }

    public void toFile(String file) {
        toFile(new File(file));
    }

    public void toFile(File file) {
        String jsonConfig = gson.toJson(this);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonConfig);
        } catch (IOException e) {
            CMDR.LOGGER.error("Failed to save config! Reason:", e);
        }
    }

    private static Config fromFile(File configFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)))) {
            return gson.fromJson(reader, Config.class);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            CMDR.LOGGER.error("Failed to read config! Reason:", e);
            return null;
        }
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}