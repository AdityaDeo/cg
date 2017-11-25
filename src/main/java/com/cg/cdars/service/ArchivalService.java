package com.cg.cdars.service;

import java.io.File;

public interface ArchivalService {
    void archive(File target) throws Exception;
    File retrieve(String archiveRecordId) throws Exception;
}
