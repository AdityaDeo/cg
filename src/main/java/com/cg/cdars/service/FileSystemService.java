package com.cg.cdars.service;

import java.io.File;
import java.util.List;

public interface FileSystemService {
    File storeToFile(List<String> lines);
    List<String> getLines(File file);
}
