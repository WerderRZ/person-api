package com.werdersoft.personapi.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;

@Component
public class FileUtils {

    public String readFile(String filePath) {
        Resource resource = new ClassPathResource(filePath);
        try {
            File file = resource.getFile();
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
