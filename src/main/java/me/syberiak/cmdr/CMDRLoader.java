package me.syberiak.cmdr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import me.syberiak.cmdr.util.JavaProcess;
import me.syberiak.cmdr.manager.ManagerMenu;

public class CMDRLoader implements ClientModInitializer {

	public static final String MOD_ID = "custom-music-discs-records";

	public static final FabricLoader FABRIC_INSTANCE = FabricLoader.getInstance();
	public static final String PACK_DIR = String.join(File.separator, FABRIC_INSTANCE.getGameDir().resolve("resourcepacks").toString(), "CMDR_RP", "");
	public static final String RECORD_DIR = PACK_DIR + String.join(File.separator, "assets", "minecraft", "sounds", "records", "");
	public static final String PACK_META_DIR = PACK_DIR + "pack.mcmeta";
	public static final String PACK_ICON_DIR = PACK_DIR + "pack.png";

	public static final ModMetadata MOD_METADATA = FABRIC_INSTANCE.getModContainer(MOD_ID).get().getMetadata();
	public static final String MOD_VERSION = MOD_METADATA.getVersion().toString();

	public static String ASSETS_DIR;
	
	public static String ICON_DIR;
	public static String SPRITES_DIR;

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		try {
			ASSETS_DIR = new File(this.getClass().getClassLoader().getResource("assets/" + MOD_ID).getFile()).toString() + File.separator;
		} catch (Exception e) {throw new RuntimeException(e);}

		ICON_DIR = ASSETS_DIR + "icon.png";
		SPRITES_DIR = ASSETS_DIR + "sprites" + File.separator;

		try {initializeResourcePack();} catch (Exception e) {throw new RuntimeException(e);}
	}

	public static void initializeResourcePack() throws IOException {
        File pack = new File(RECORD_DIR);
        if (!pack.exists()) {
            LOGGER.info("Can't find CMDR's RP, generating new one...");

            pack.mkdirs();

            LOGGER.info("CMDR's RP: generated successfully.");
        }
        File pack_meta = new File(PACK_META_DIR);

        if (!pack_meta.exists()) {
            LOGGER.info("Can't find RP's pack.mcmeta, generating new one...");

            JsonWriter writer = new JsonWriter(new FileWriter(pack_meta));
			writer.setIndent("  ");
            writer.beginObject()
                  .name("pack").beginObject()
                  .name("pack_format").value(8)
                  .name("description").value("A CMDR-generated resource pack.")
				  .endObject()
            .endObject();
			writer.close();

            LOGGER.info("RP's pack.mcmeta: generated successfully.");
        }
        File pack_icon = new File(PACK_ICON_DIR);
        
        if (!pack_icon.exists()) {
            LOGGER.info("Can't find RP's pack icon, generating new one...");

            Files.copy(Paths.get(ICON_DIR), Paths.get(PACK_ICON_DIR), StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info("RP's pack icon: generated successfully.");
        }
    }

	public static void openManager() {
		try {
			JavaProcess.exec(ManagerMenu.class, Arrays.asList(RECORD_DIR, ICON_DIR, SPRITES_DIR, MOD_VERSION));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
}
