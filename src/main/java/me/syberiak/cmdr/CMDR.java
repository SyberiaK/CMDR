package me.syberiak.cmdr;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.syberiak.cmdr.config.Config;
import me.syberiak.cmdr.rp.ResourcePack;
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
    public static String DEFAULT_PRISM_PATH = CURRENT_USER_ROAMING + "/PrismLauncher";

    // Define settings and logs directories
    public static String APPDATA_DIR = IS_WINDOWS ? System.getenv("LOCALAPPDATA") : CURRENT_USER_ROAMING;
    public static final String SETTINGS_DIR = APPDATA_DIR + "/CMDR/config.json";
    static {
        System.setProperty("CMDR.logsDir", APPDATA_DIR + "/CMDR/logs");
    }

    // Define RP directory
    public static String RECORD_DIR;

    public static ManagerMenu manager;

    public static final Logger LOGGER = LoggerFactory.getLogger(CMDR.class);

    public static void main(String[] args) {
        try (InputStream musicDataStream = CMDR.class.getResourceAsStream("/csv/music-discs-data.csv")) {
            MUSIC_DATA = CSVReader.readDataFromCSV(musicDataStream, ",");
        } catch (Exception e) {
            throwError(e);
        }

        Config.load(SETTINGS_DIR);
        syncWithConfig();
        Config config = Config.getInstance();

        if (config.getVanillaPath().equals("<UNDEFINED>")) {
            LOGGER.warn("CMDR cannot find the Minecraft directory. " +
                    "Please set the path to the game directory manually in settings.");
            JOptionPane.showMessageDialog(manager,
                    "CMDR cannot find the Minecraft directory.\n" +
                            "Please set the path to the game directory manually in settings.",
                    "CMDR Manager", JOptionPane.WARNING_MESSAGE);
        }

        ResourcePack.initialize(config.getVanillaPath());

        SwingUtilities.invokeLater(() -> {
            manager = new ManagerMenu();
            manager.setVisible(true);
        });

        LOGGER.info("Launched successfully.");
    }

    public static void validateConfig() {
        Config config = Config.getInstance();

        if (!new File(config.getVanillaPath()).exists()) {
            config.setVanillaPath("<UNDEFINED>");
        }
        if (!new File(config.getPrismPath()).exists()) {
            config.setPrismPath("<UNDEFINED>");
        }
    }

    public static void syncWithConfig() {
        validateConfig();

        // RECORD_DIR =  "/assets/minecraft/sounds/records";
    }

    public static boolean isMinecraftDirectory(String path) {
        return new File(path + "/resourcepacks").exists();
    }

    public static String[] parsePrismInstances() {
        String prismPath = Config.getInstance().getPrismPath();

        try (Stream<Path> instancesPaths = Files.find(Paths.get(prismPath + "/instances"),
                1,
                (path, attr) -> Files.isDirectory(path) &&
                        !path.toString().equals(".LAUNCHER_TEMP"))) {
            return instancesPaths.map(path -> path.getFileName().toString()).toArray(String[]::new);
        } catch (Exception e) {
            LOGGER.error("Failed parsing Prism instances:", e);
        }
        return new String[0];
    }

    public static void throwError(Exception e) {
        LOGGER.error("Error occurred!", e);
        throw new RuntimeException(e);
    }
}