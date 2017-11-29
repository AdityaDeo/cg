package com.cg.cdars.controller;

import com.cg.cdars.model.ArchivedRecord;
import com.cg.cdars.model.DataSet;
import com.cg.cdars.model.ScriptType;
import com.cg.cdars.service.ArchivalService;
import com.cg.cdars.service.DataExtractionService;
import com.cg.cdars.service.FileSystemService;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.File;
import java.text.DateFormat;
import java.util.List;

public class RestController {
    private FileSystemService fileSystemService;
    private DataExtractionService dataExtractionService;
    private ArchivalService archivalService;

    private NamedParameterJdbcTemplate defaultJdbc;
    private DateFormat dateFormat;

    void archive(String dataSetName, String startDate, String endDate, String scriptType) throws Exception {
        List<String> insertStatements = dataExtractionService.generateSqlStatementsForDataSet(defaultJdbc, dataSetName,
                dateFormat.parse(startDate), dateFormat.parse(endDate), ScriptType.valueOf(scriptType));

        File outputFile = fileSystemService.storeToFile("", insertStatements);

        archivalService.archive(outputFile);

        //TODO: call archive records DAO
    }

    void retrieve(String archivalRecordId) throws Exception {
        File file = archivalService.retrieve(archivalRecordId);
        List<String> lines = fileSystemService.getLines(file);
        dataExtractionService.loadData(defaultJdbc, lines);
    }

    List<DataSet> getDataSets() {
        return null;
    }

    List<ArchivedRecord> getArchiveRecords(String dataSet, String startDate, String endDate) {
        return null;
    }
}
