package com.cg.cdars.service;

import java.io.File;

public interface ArchivalService {
    String archive(File target) throws Exception;
    File retrieve(String fileName, String archiveRecordId) throws Exception;
}
