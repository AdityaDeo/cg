package com.cg.cdars.service.impl;

import com.cg.cdars.service.FileSystemService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class FileSystemServiceImpl implements FileSystemService {
    private static final String POSTFIX = EMPTY;

    @Override
    public File storeToFile(String fileNamePrefix, List<String> lines) throws IOException {
        Path targetPath = Files.createTempFile(fileNamePrefix, POSTFIX);
        Files.write(targetPath, lines);
        return new File(targetPath.toString());
    }

    @Override
    public List<String> getLines(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }
}
