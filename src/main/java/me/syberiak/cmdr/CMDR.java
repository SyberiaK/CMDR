package me.syberiak.cmdr;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.syberiak.cmdr.settings.*;
import me.syberiak.cmdr.ui.ManagerMenu;
import me.syberiak.cmdr.util.CSVReader;

public class CMDR {

    public static String VERSION = "0.1.0";
    public static String[] CHANGELOG = {"- Added logging",
                                        "- System look and feel",
                                        "- MacOS and Linux support (not tested)",
                                        "- Code improvements",
                                        "- Exception notifs improvements",
                                        "- OGG audio support"};
    public static final URL ICON_URL = CMDR.class.getResource("/icon.png");
    public static List<String[]> MUSIC_DATA;

    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static String CURRENT_USER_ROAMING;
    static {
        CURRENT_USER_ROAMING = System.getProperty("user.home");

        if (OS.contains("mac") || OS.contains("darwin")) {
            CURRENT_USER_ROAMING += "/Library/Application Support";
        } else if (OS.contains("win")) {
            CURRENT_USER_ROAMING = System.getenv("APPDATA");
        }
    }
    public static String DEFAULT_MINECRAFT_PATH;
    static {
        DEFAULT_MINECRAFT_PATH = CURRENT_USER_ROAMING + File.separator;
        if (OS.contains("mac")) {
            DEFAULT_MINECRAFT_PATH += "minecraft";
        } else {
            DEFAULT_MINECRAFT_PATH += ".minecraft";
        }
    }

    public static String SETTINGS_DIR;
    public static String MINECRAFT_PATH;

    static String PACK_DIR;
    public static String RECORD_DIR;
    static String PACK_META_DIR;
    static String PACK_ICON_DIR;

    public static ManagerMenu manager;

    static String logFileLocation;
    static {
        if (OS.contains("win")) {
            logFileLocation = System.getenv("LOCALAPPDATA");
        } else {
            logFileLocation = CURRENT_USER_ROAMING;
        }
        logFileLocation += "/CMDR/logs";
        System.setProperty("CMDR.logs", logFileLocation);
    }
    public static final Logger LOGGER = LoggerFactory.getLogger(CMDR.class);


    public static void main(String[] args) {


        try (InputStream musicDataStream = CMDR.class.getResourceAsStream("/csv/music-discs-data.csv")) {
            MUSIC_DATA = CSVReader.readDataFromCSV(musicDataStream, ",");
        } catch (Exception e) {
            LOGGER.error("Error occurred!", e);
            throw new RuntimeException(e);
        }

        try {
            File PROGRAM_FOLDER = new File(CMDR.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile();

            SETTINGS_DIR = new File(PROGRAM_FOLDER + File.separator + "settings.json").getPath();
            File settings_file = new File(SETTINGS_DIR);

            if (settings_file.createNewFile()) {
                JSONObject settingsObject = new JSONObject();
                settingsObject.put("pathToMinecraft", "<PATH HERE>");

                try (FileWriter filewriter = new FileWriter(settings_file)) {
                    filewriter.write(settingsObject.toString());
                }
            }

            getSettings();
            initializeResourcePack();
        } catch (Exception e) {
            LOGGER.error("Error occurred!", e);
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(() -> {
            manager = new ManagerMenu();
            manager.setVisible(true);
        });

        try {
            if (Settings.readSettings(SETTINGS_DIR).getPathToMinecraft().equals("<NOT DEFAULT>")) {
                LOGGER.warn("CMDR cannot find the Minecraft directory. " +
                        "Please set the path to the game directory manually in settings.");
                JOptionPane.showMessageDialog(manager,
                        "CMDR cannot find the Minecraft directory.\n" +
                                "Please set the path to the game directory manually in settings.",
                        "CMDR Manager", JOptionPane.WARNING_MESSAGE);
            }
            LOGGER.info("Launched successfully.");
        } catch (Exception e) { LOGGER.warn("Exception occurred!", e); }
    }

    public static void getSettings() {
        try {
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
            PACK_DIR = MINECRAFT_PATH + String.join(File.separator,
                    "", "resourcepacks", "CMDR", "");
            RECORD_DIR = PACK_DIR + String.join(File.separator,
                    "assets", "minecraft", "sounds", "records", "");
            PACK_META_DIR = PACK_DIR + "pack.mcmeta";
            PACK_ICON_DIR = PACK_DIR + "pack.png";

        } catch (Exception e) {
            LOGGER.error("Error occurred!", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean isMinecraftDirectory(Path path) {
        return new File(path + File.separator + "resourcepacks").exists();
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

        File pack_meta = new File(PACK_META_DIR);

        if (!pack_meta.exists()) {
            LOGGER.info("Can't find RP's pack.mcmeta, generating new one...");

            JSONObject packObject = new JSONObject();
            packObject.put("pack_format", 8);
            packObject.put("description", "Autogenerated resource pack. (Ignore incompatibility message)");

            JSONObject metaObject = new JSONObject();
            metaObject.put("pack", packObject);

            try (FileWriter filewriter = new FileWriter(pack_meta)) {
                filewriter.write(metaObject.toString());
                LOGGER.info("RP's pack.mcmeta: generated successfully.");
            } catch (Exception e) {
                LOGGER.error("RP's pack.mcmeta: failed generating.");
            }
        }

        File pack_icon = new File(PACK_ICON_DIR);

        if (!pack_icon.exists()) {
            LOGGER.info("Can't find RP's pack icon, generating new one...");

            try (InputStream iconStream = CMDR.class.getResourceAsStream("/icon.png")) {
                assert iconStream != null;
                Files.copy(iconStream, pack_icon.toPath());
                LOGGER.info("RP's pack icon: generated successfully.");
            } catch (Exception e) {
                LOGGER.error("RP's pack icon: failed generating.");
            }
        }
    }
}