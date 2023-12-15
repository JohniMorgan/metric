package ru.spb.nicetu.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FileService implements IFileService {
    private static final Logger logger = LoggerFactory.getLogger(IFileService.class);
    private File target;
    private boolean continiuos;
    public FileService(String path) {
        continiuos = initFile(path);
    }
    private boolean initFile(String path) {
        logger.info("Start finding metrics file");
        String directorySlash = System.getProperty("os.name").contains("Windows") ? "\\" : "/";
        File directory = new File(path);
        if (!directory.exists()) directory.mkdir();
        target = new File(path + directorySlash + "metrics-" + ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("uuuu-MM-dd")) + ".json");
        boolean exist = false;
        try {
            exist = target.exists();
            if (!exist) {
                logger.warn("File doesn't exist. Creating file...");
                target.createNewFile();
            }
        } catch (Exception e) {
            logger.error("file error caused {}", e.getMessage());
        }
        return exist;
    }
    public boolean isExisted() {
       return continiuos;
    }

    @Override
    public void write(String content) {
        logger.debug("Start write into file");
        try {
            FileWriter writer = new FileWriter(target);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("Write to file error caused {}", e.getMessage());
        }
    }

    public File getTarget() {
        return target;
    }
}
