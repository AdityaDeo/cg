package com.cg.cdars.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileSystemService {
    File storeToFile(String fileNamePrefix, List<String> lines) throws IOException;

    List<String> getLines(File file) throws IOException;
}
