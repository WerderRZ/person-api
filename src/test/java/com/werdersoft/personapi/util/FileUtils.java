package com.werdersoft.personapi.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;

@Component
public class FileUtils {

    public String readFile(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        File file = resource.getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}
