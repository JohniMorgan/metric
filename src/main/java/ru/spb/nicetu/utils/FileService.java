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
        logger.info("Запущен поиск файла метрик");
        File directory = new File(path);
        if (!directory.exists()) directory.mkdir();
        target = new File(path + File.separator + "metrics-" + ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("uuuu-MM-dd")) + ".json");
        boolean exist = false;
        try {
            exist = target.exists();
            if (!exist) {
                logger.warn("Файл не существует! Идёт попытка создания файла...");
                if (target.createNewFile()) logger.info("Файл был успешно создан");
            } else logger.info("Файл обнаружен, получения доступа");
        } catch (Exception e) {
            logger.error("Ошибка обращения к файлу. Причина: {}", e.getMessage());
        }
        return exist;
    }
    public boolean isExisted() {
       return continiuos;
    }

    @Override
    public void write(String content) {
        logger.debug("Инициализация потока записи в файл");
        FileWriter writer = null;
        try {
            writer = new FileWriter(target);
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            logger.error("Ошибка записи файла. Причина: {}", e.getMessage());
        } finally {
            if (writer != null) try {
                writer.close();
            }
            catch(Exception er) {
                logger.error("Ошибка закрытия файла. Причина: {}", er.getMessage());
            }
        }
    }

    public File getTarget() {
        return target;
    }
}
