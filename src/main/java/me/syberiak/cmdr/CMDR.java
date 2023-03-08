package me.syberiak.cmdr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.syberiak.cmdr.settings.*;
import me.syberiak.cmdr.ui.ManagerMenu;
import me.syberiak.cmdr.util.CSVReader;

public class CMDR {

    public static final String VERSION = "0.2.1";
    public static final String VERSION_V = "v" + VERSION;

    public static final String[] MAJOR_CHANGELOG = {"Major changelog (0.1.0):",
                                                    "- Added logging",
                                                    "- System look and feel",
                                                    "- MacOS and Linux support (not tested)",
                                                    "- Code improvements",
                                                    "- Exception notifications improvements",
                                                    "- OGG audio support"};
    public static final String[] LATEST_CHANGELOG = {"Latest changelog:",
                                                     "- Fixed CSVReader unable to parse non-ASCII characters",
                                                     "- Fixed typo in Samuel Åberg's name",
                                                     "- Various fixes for MacOS and Unix",
                                                     "- App crashes if using other OS than Windows, MacOS or Unix"};

    public static final URL ICON_URL = CMDR.class.getResource("/icon.png");
    public static List<String[]> MUSIC_DATA;

    // Define OS
    public static final String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
    public static final boolean IS_WINDOWS = OS.contains("windows");
    public static final boolean IS_MAC = OS.contains("mac") || OS.contains("darwin");

    public static final boolean IS_UNIX = OS.contains("nix") || OS.contains("nux") || OS.contains("aix");

    public static final String CURRENT_USER_ROAMING = defineAppDataDir();
    static String defineAppDataDir() {
        if (IS_UNIX) return System.getProperty("user.home");

        if (IS_MAC) return System.getProperty("user.home") + "/Library/Application Support";

        if (IS_WINDOWS) return System.getenv("APPDATA");

        throwError(new Exception("Unsupported platform: " + OS));
        return "";
    }


    public static final String[] SUPPORTED_LAUNCHERS = {"Vanilla",
                                                        "Prism"};

    // Define default Minecraft directory
    public static String DEFAULT_MINECRAFT_PATH = CURRENT_USER_ROAMING + (IS_MAC ? "/minecraft" : "/.minecraft");
    // Currently Windows-only
    public static String DEFAULT_PRISM_PATH = System.getenv("LOCALAPPDATA") + "/Programs/PrismLauncher";

    // Define settings and logs directories
    public static final String SETTINGS_DIR;
    static {
        String LOCALAPPDATA = IS_WINDOWS ? System.getenv("LOCALAPPDATA") : CURRENT_USER_ROAMING;

        SETTINGS_DIR = LOCALAPPDATA + "/CMDR/settings.json";
        System.setProperty("CMDR.logsDir", LOCALAPPDATA + "/CMDR/logs");
    }

    // Define RP directory
    public static String MINECRAFT_PATH;
    private static String PACK_DIR;
    public static String RECORD_DIR;

    public static ManagerMenu manager;

    public static final Logger LOGGER = LoggerFactory.getLogger(CMDR.class);

    public static void main(String[] args) {
        try (InputStream musicDataStream = CMDR.class.getResourceAsStream("/csv/music-discs-data.csv")) {
            MUSIC_DATA = CSVReader.readDataFromCSV(musicDataStream, ",");
        } catch (Exception e) {
            throwError(e);
        }

        File settings_file = new File(SETTINGS_DIR);

        try {
            if (settings_file.createNewFile()) {
                SettingsContainer settings = new SettingsContainer("<PATH HERE>");
                Settings.editSettings(SETTINGS_DIR, settings);
            }

            updateSettings();
        } catch (Exception e) {
            throwError(e);
        }

        initializeResourcePack();

        SwingUtilities.invokeLater(() -> {
            manager = new ManagerMenu();
            manager.setVisible(true);
        });

        try {
            if (MINECRAFT_PATH.equals("<NOT DEFAULT>")) {
                LOGGER.warn("CMDR cannot find the Minecraft directory. " +
                        "Please set the path to the game directory manually in settings.");
                JOptionPane.showMessageDialog(manager,
                        "CMDR cannot find the Minecraft directory.\n" +
                                "Please set the path to the game directory manually in settings.",
                        "CMDR Manager", JOptionPane.WARNING_MESSAGE);
            }
            LOGGER.info("Launched successfully.");
        } catch (Exception e) {
            LOGGER.warn("Exception occurred!", e);
        }
    }

    public static void updateSettings() throws IOException {
        SettingsContainer settings = Settings.readSettings(SETTINGS_DIR);

        MINECRAFT_PATH = settings.getPathToMinecraft();
        if (MINECRAFT_PATH.equals("<PATH HERE>")) {
            if (!new File(DEFAULT_MINECRAFT_PATH).exists()) {
                DEFAULT_MINECRAFT_PATH = "<NOT DEFAULT>";
            }
            settings.setPathToMinecraft(DEFAULT_MINECRAFT_PATH);
            Settings.editSettings(SETTINGS_DIR, settings);
            MINECRAFT_PATH = settings.getPathToMinecraft();
        }
        PACK_DIR = MINECRAFT_PATH + "/resourcepacks/CMDR/";
        RECORD_DIR = PACK_DIR + "assets/minecraft/sounds/records/";
    }

    public static void editAndUpdateSettings(SettingsContainer settings) throws IOException {
        Settings.editSettings(SETTINGS_DIR, settings);

        updateSettings();
    }

    public static boolean isMinecraftDirectory(Path path) {
        return new File(path + "/resourcepacks").exists();
    }

    public static void initializeResourcePack() {
        File pack = new File(RECORD_DIR);
        if (!pack.exists()) {
            LOGGER.info("Can't find RP, generating new one...");

            if (pack.mkdirs()) {
                LOGGER.info("RP: generated successfully.");
            } else {
                LOGGER.error("RP: failed generating.");
            }
        }

        File pack_meta = new File(PACK_DIR + "pack.mcmeta");

        if (!pack_meta.exists()) {
            LOGGER.info("Can't find RP's pack.mcmeta, generating new one...");

            JSONObject packObject = new JSONObject();
            packObject.put("pack_format", 9);
            packObject.put("description", "Autogenerated resource pack. (Ignore incompatibility message)");

            JSONObject metaObject = new JSONObject();
            metaObject.put("pack", packObject);

            try (FileWriter filewriter = new FileWriter(pack_meta)) {
                filewriter.write(metaObject.toString());
                LOGGER.info("RP's pack.mcmeta: generated successfully.");
            } catch (Exception e) {
                LOGGER.error("RP's pack.mcmeta: failed generating. Reason:", e);
            }
        }

        File pack_icon = new File(PACK_DIR + "pack.png");

        if (!pack_icon.exists()) {
            LOGGER.info("Can't find RP's pack icon, generating new one...");

            try (InputStream iconStream = CMDR.class.getResourceAsStream("/icon.png")) {
                assert iconStream != null;
                Files.copy(iconStream, pack_icon.toPath());
                LOGGER.info("RP's pack icon: generated successfully.");
            } catch (Exception e) {
                LOGGER.error("RP's pack icon: failed generating. Reason:", e);
            }
        }
    }

    public static void throwError(Exception e) {
        LOGGER.error("Error occurred!", e);
        throw new RuntimeException(e);
    }
}