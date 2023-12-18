package ru.spb.nicetu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

public class JSONProvider implements IJSONProvider {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JSONProvider.class);

    @Override
    public Object parse(Class t, IFileService jsonInFile) {
        logger.debug("Старт десерализации JSON  в объект");
        Object result = null;
        try {
            result = objectMapper.readValue(jsonInFile.getTarget(), t);
        } catch (Exception e) {
            logger.error("Ошибка десериализации. Причина: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public String stringify(Object t) {
        logger.debug("Старт сериализации объекта в JSON");
        this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        StringWriter sw = new StringWriter();
        String result = null;
        try {
            objectMapper.writeValue(sw, t);
            result = sw.toString();
        } catch (IOException e) {
            logger.error("Ошибка сериализации. Причина: {}", e.getMessage());
        }
        return result;
    }
}
