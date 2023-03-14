package me.syberiak.cmdr.rp;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;

import org.json.JSONObject;
import me.syberiak.cmdr.CMDR;

public class ResourcePack {
    private static final String packPlacement = "/resourcepacks/CMDR";
    private static final String recordsPlacement = "/assets/minecraft/sounds/records";
    private static final String metaPlacement = "/pack.mcmeta";
    private static final String iconPlacement = "/pack.png";
    public static void initialize(String path) {
        String packDir = path + packPlacement;
        File pack = new File(packDir + recordsPlacement);
        if (!pack.exists()) {
            if (pack.mkdirs()) {
                CMDR.LOGGER.info("RP: generated successfully.");
            } else {
                CMDR.LOGGER.error("RP: failed generating.");
            }
        }

        File packMeta = new File(packDir + metaPlacement);

        if (!packMeta.exists()) {
            JSONObject metaObject = new JSONObject();

            JSONObject packObject = new JSONObject();
            packObject.put("pack_format", 9);
            packObject.put("description", "Autogenerated resource pack. (Ignore incompatibility message)");

            metaObject.put("pack", packObject);

            try (FileWriter filewriter = new FileWriter(packMeta)) {
                filewriter.write(metaObject.toString());
                CMDR.LOGGER.info("RP's pack.mcmeta: generated successfully.");
            } catch (Exception e) {
                CMDR.LOGGER.error("RP's pack.mcmeta: failed generating. Reason:", e);
            }
        }

        File packIcon = new File(packDir + iconPlacement);

        if (!packIcon.exists()) {
            try (InputStream iconStream = CMDR.class.getResourceAsStream("/icon.png")) {
                assert iconStream != null;
                Files.copy(iconStream, packIcon.toPath());
                CMDR.LOGGER.info("RP's pack icon: generated successfully.");
            } catch (Exception e) {
                CMDR.LOGGER.error("RP's pack icon: failed generating. Reason:", e);
            }
        }
    }
}