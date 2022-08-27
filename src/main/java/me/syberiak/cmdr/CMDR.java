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

import me.syberiak.cmdr.ui.ManagerMenu;
import me.syberiak.cmdr.util.CSVReader;
import me.syberiak.cmdr.util.StreamUtil;
import me.syberiak.cmdr.settings.Settings;
import me.syberiak.cmdr.settings.SettingsContainer;

public class CMDR {

    public static String VERSION = "0.0.1";
    public static final URL ICON_URL = CMDR.class.getResource("/icon.png");
    public static List<String[]> MUSIC_DATA;

    public static final String CURRENT_USER_ROAMING = System.getenv("APPDATA");

    public static String SETTINGS_DIR;
    public static String MINECRAFT_PATH;

    static String PACK_DIR;
    public static String RECORD_DIR;
    static String PACK_META_DIR;
    static String PACK_ICON_DIR;

    public static ManagerMenu manager;

    public static void main(String[] args) {

        try (InputStream musicDataStream = CMDR.class.getResourceAsStream("/csv/music-discs-data.csv")){
            File musicDataTempFile = StreamUtil.streamToFile(musicDataStream);
            MUSIC_DATA = CSVReader.readDataFromCSV(musicDataTempFile.getAbsolutePath(), ",");
        } catch (Exception e) { throw new RuntimeException(e); }

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
        } catch (Exception e) { throw new RuntimeException(e); }

        SwingUtilities.invokeLater(() -> {
            manager = new ManagerMenu();
            manager.setVisible(true);
        });

        try {
            if (Settings.readSettings(SETTINGS_DIR).getPathToMinecraft().equals("<NOT DEFAULT>")) {
                JOptionPane.showMessageDialog(manager,
                        "CMDR cannot find the Minecraft directory." +
                                "Please set the path to the game directory manually in settings.",
                        "CMDR Manager", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) { throw new RuntimeException(e); }

    }

    public static void getSettings() {
        try {
            SettingsContainer settings = Settings.readSettings(SETTINGS_DIR);

            MINECRAFT_PATH = settings.getPathToMinecraft();
            if (MINECRAFT_PATH.equals("<PATH HERE>")) {
                String defaultMinecraftPath = CURRENT_USER_ROAMING + File.separator + ".minecraft";
                if (!new File(defaultMinecraftPath).exists()) {
                    defaultMinecraftPath = "<NOT DEFAULT>";
                }
                settings.setPathToMinecraft(defaultMinecraftPath);
                Settings.editSettings(SETTINGS_DIR, settings);
                MINECRAFT_PATH = settings.getPathToMinecraft();
            }
            PACK_DIR = MINECRAFT_PATH + String.join(File.separator,
                    "", "resourcepacks", "CMDR", "");
            RECORD_DIR = PACK_DIR + String.join(File.separator,
                    "assets", "minecraft", "sounds", "records", "");
            PACK_META_DIR = PACK_DIR + "pack.mcmeta";
            PACK_ICON_DIR = PACK_DIR + "pack.png";

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static boolean isMinecraftDirectory(Path path) {
        return new File(path + File.separator + "resourcepacks").exists();
    }

    public static void initializeResourcePack() {
        File pack = new File(RECORD_DIR);
        if (!pack.exists()) {
            System.out.println("Can't find CMDR's RP records folder, generating new one...");

            if (pack.mkdirs()) {
                System.out.println("CMDR's RP records folder: generated successfully.");
            } else {
                System.out.println("CMDR's RP records folder: failed generating.");
            }
        }

        File pack_meta = new File(PACK_META_DIR);

        if (!pack_meta.exists()) {
            System.out.println("Can't find RP's pack.mcmeta, generating new one...");

            JSONObject packObject = new JSONObject();
            packObject.put("pack_format", 8);
            packObject.put("description", "A CMDR - generated resource pack.");

            JSONObject metaObject = new JSONObject();
            metaObject.put("pack", packObject);

            try (FileWriter filewriter = new FileWriter(pack_meta)) {
                filewriter.write(metaObject.toString());
                System.out.println("RP's pack.mcmeta: generated successfully.");
            } catch (Exception e) {
                System.out.println("RP's pack.mcmeta: failed generating.");
            }
        }
        File pack_icon = new File(PACK_ICON_DIR);

        if (!pack_icon.exists()) {
            System.out.println("Can't find RP's pack icon, generating new one...");

            try (InputStream iconStream = CMDR.class.getResourceAsStream("/icon.png")) {
                assert iconStream != null;
                Files.copy(iconStream, pack_icon.toPath());
                System.out.println("RP's pack icon: generated successfully.");
            } catch (Exception e) {
                System.out.println("RP's pack icon: failed generating.");
            }
        }
    }
}