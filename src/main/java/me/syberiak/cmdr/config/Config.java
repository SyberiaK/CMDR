package me.syberiak.cmdr.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.syberiak.cmdr.CMDR;

public class Config extends ConfigWrapper {
    AppConfig app;
    Bindings bindings;

    public Config() {
        app = new AppConfig();
        bindings = new Bindings();
    }

    public String getLanguage() {
        return app.lang;
    }
    public Launcher getSelectedLauncher() {
        return Launcher.findLauncherByID(bindings.selectedLauncher);
    }

    public void selectLauncher(Launcher launcher) {
        bindings.selectedLauncher = launcher.id;
    }
    public Path getVanillaPath() {
        return Paths.get(bindings.vanilla.path);
    }

    public void setVanillaPath(Path path) {
        setVanillaPath(path.toString());
    }

    public void setVanillaPath(String path) {
        bindings.vanilla.path = path;
    }

    public Path getPrismPath() {
        return Paths.get(bindings.prism.path);
    }

    public void setPrismPath(String path) {
        bindings.vanilla.path = path;
    }

    public String[] getPrismInstances() {
        return bindings.prism.instances;
    }
    public void setPrismInstances(String[] instances) {
        bindings.prism.instances = instances;
    }

    public Path[] getPrismInstancesPaths() {
        Path prismPath = getPrismPath();
        String[] instances = getPrismInstances();

        Path[] instancesPaths = new Path[instances.length];

        for (int i = 0; i < instances.length; i++) {
            instancesPaths[i] = Paths.get(prismPath + "/instances", instances[i]);
        }

        return instancesPaths;
    }

    // Container classes
    static class AppConfig {
        String lang;

        AppConfig() {
            lang = Locale.getDefault().toLanguageTag();
        }
    }

    static class Bindings {

        String selectedLauncher;
        VanillaBindings vanilla;
        PrismBindings prism;

        Bindings() {
            selectedLauncher = Launcher.Vanilla.id;
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
                path = CMDR.DEFAULT_PRISM_PATH;
                instances = new String[0];
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