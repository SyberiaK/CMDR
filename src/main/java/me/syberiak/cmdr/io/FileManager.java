package me.syberiak.cmdr.io;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.config.Config;
import me.syberiak.cmdr.util.OGGAudioConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static me.syberiak.cmdr.CMDR.LOGGER;

public class FileManager {

    public static boolean isVanillaDirectory(Path path) {
        // Multiple checks to allow fewer false positives
        boolean assetsExist = new File(path + "/assets").exists();
        boolean binExist = new File(path + "/bin").exists();
        boolean savesExist = new File(path + "/saves").exists();
        boolean resourcepacksExist = new File(path + "/resourcepacks").exists();

        return assetsExist && binExist && savesExist && resourcepacksExist;
    }

    public static boolean isPrismDirectory(Path path) {
        // Multiple checks to allow fewer false positives
        boolean assetsExist = new File(path + "/assets").exists();
        boolean cfgExist = new File(path + "/prismlauncher.cfg").exists();
        boolean instancesExist = new File(path + "/instances").exists();

        return assetsExist && cfgExist && instancesExist;
    }

    public static Path[] getPrismInstancesPaths() {
        Path prismPath = Config.getInstance().getPrismPath();
        Path instancesDir = Paths.get(prismPath + "/instances");

        try (Stream<Path> instancesPaths = Files.walk(instancesDir, 1)
                .filter(
                        p -> Files.isDirectory(p) &&
                                !p.equals(instancesDir) &&
                                !p.getFileName().toString().equals(".LAUNCHER_TEMP")
                )
        ) {
            return instancesPaths.toArray(Path[]::new);
        } catch (IOException e) {
            LOGGER.error("Failed parsing Prism instances:", e);
        }
        return new Path[0];
    }

    public static String[] getPrismInstances() {
        Path[] instancesPaths = getPrismInstancesPaths();

        return Arrays.stream(instancesPaths)
                .map(p -> p.getFileName().toString()).toArray(String[]::new);
    }

    public static void setCustomRecordForVanilla(File source, String recordName) throws Exception {
        File target = new File(
                Config.getInstance().getVanillaPath() + ResourcePack.recordsPlacement + recordName);
        OGGAudioConverter.convert(source, target);
        CMDR.LOGGER.info("{}: converted successfully to {}", source, target);
    }

    public static void setCustomRecordForPrism(File source, String recordName) throws Exception {
        Path[] targetPaths = Config.getInstance().getPrismInstancesPaths();
        File target = new File(targetPaths[0] + ResourcePack.recordsPlacement + recordName);
        OGGAudioConverter.convert(source, target);
        CMDR.LOGGER.info("{}: converted successfully to {}", source, target);

        targetPaths = Arrays.copyOfRange(targetPaths, 1, targetPaths.length);
        for (Path path : targetPaths) {
            path = Paths.get(path + ResourcePack.recordsPlacement + recordName);

            Files.copy(target.toPath(), path);
            CMDR.LOGGER.info("{}: copied successfully to {}", target, path);
        }
    }

    public static void revertRecordForVanilla(String recordName) {
        Path target = Paths.get(
                Config.getInstance().getVanillaPath() + ResourcePack.recordsPlacement + recordName);
        if (!target.toFile().exists()) {
            CMDR.LOGGER.warn("{}: is default record already.", target);
            return;
        }

        try {
            Files.delete(target);
            CMDR.LOGGER.info("{}: returned default record successfully.", target);
        } catch (Exception e) {
            CMDR.LOGGER.error("{}: failed to return default record. Reason: {}", target, e);
        }
    }

    public static void revertRecordForPrism(String recordName) {
        Path[] targetPaths = Config.getInstance().getPrismInstancesPaths();
        for (Path path : targetPaths) {
            Path target = Paths.get(path + ResourcePack.recordsPlacement + recordName);
            if (!target.toFile().exists()) {
                CMDR.LOGGER.warn("{}: is default record already.", target);
                continue;
            }

            try {
                Files.delete(target);
                CMDR.LOGGER.info("{}: returned default record successfully.", target);
            } catch (Exception e) {
                CMDR.LOGGER.error("{}: failed to return default record. Exception: {}", target, e);
            }
        }
    }
}
