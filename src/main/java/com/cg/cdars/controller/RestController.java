package com.cg.cdars.controller;

import com.cg.cdars.dao.GenericDao;
import com.cg.cdars.domain.ArchiveRecord;
import com.cg.cdars.domain.DataSet;
import com.cg.cdars.domain.ScriptType;
import com.cg.cdars.service.ArchivalService;
import com.cg.cdars.service.DataExtractionService;
import com.cg.cdars.service.FileSystemService;

import java.io.File;
import java.text.DateFormat;
import java.util.List;

public class RestController {
    private FileSystemService fileSystemService;
    private DataExtractionService dataExtractionService;
    private ArchivalService archivalService;

    private GenericDao defaultDao;
    private DateFormat dateFormat;

    void archive(String dataSetType, String startDate, String endDate, String scriptType) throws Exception {
        List<String> insertStatements = dataExtractionService.generateSqlStatementsForDataSet(defaultDao, DataSet.DataSetType.valueOf(dataSetType),
                dateFormat.parse(startDate), dateFormat.parse(endDate), ScriptType.valueOf(scriptType));

        File outputFile = fileSystemService.storeToFile(insertStatements);

        archivalService.archive(outputFile);

        //TODO: call archive records DAO
    }

    void retrieve(String archivalRecordId) throws Exception {
        File file = archivalService.retrieve(archivalRecordId);
        List<String> lines = fileSystemService.getLines(file);
        dataExtractionService.loadData(defaultDao, lines);
    }

    List<DataSet> getDataSets() {
        return null;
    }

    List<ArchiveRecord> getArchiveRecords(String dataSet, String startDate, String endDate) {
        return null;
    }
}
