package com.werdersoft.personapi.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class JsonFileReaderService {
    private final ResourceLoader resourceLoader;

    public JsonFileReaderService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String readJsonFile(String filePath) throws IOException {
        // Получаем ресурс из папки resources
        Resource resource = resourceLoader.getResource("classpath:" + filePath);

        // Получаем InputStream из ресурса
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // Читаем содержимое файла
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            return content.toString();
        }
    }

}
