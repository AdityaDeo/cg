package com.cg.cdars.controller;

import com.cg.cdars.dao.ArchivedRecordDao;
import com.cg.cdars.dao.DataSetDao;
import com.cg.cdars.model.ArchivedRecord;
import com.cg.cdars.model.DataSet;
import com.cg.cdars.model.ScriptType;
import com.cg.cdars.service.ArchivalService;
import com.cg.cdars.service.DataExtractionService;
import com.cg.cdars.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

@RestController
public class CdarsController {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss");
    private static final String FILE_NAME_PREFIX_FORMAT = "DataSetArchive_%s_";

    @Autowired
    private DataExtractionService dataExtractionService;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private ArchivalService archivalService;

    @Autowired
    private ArchivedRecordDao archivedRecordDao;

    @Autowired
    private DataSetDao dataSetDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @RequestMapping("/archiveDataSet/{dataSetName}/{strStartDate}/{strEndDate}/{strScriptType}")
    @ResponseBody
    public ArchivedRecord archiveDataSet(@PathVariable String dataSetName,
                               @PathVariable String strStartDate,
                               @PathVariable String strEndDate,
                               @PathVariable String strScriptType) throws Exception {

        Date startDate = simpleDateFormat.parse(strStartDate);
        Date endDate = simpleDateFormat.parse(strEndDate);
        ScriptType scriptType = ScriptType.valueOf(strScriptType);

        List<String> lines = dataExtractionService.generateSqlStatementsForDataSet(jdbc,
                dataSetName,
                startDate,
                endDate,
                scriptType);

        File output = fileSystemService.storeToFile(format(FILE_NAME_PREFIX_FORMAT, SIMPLE_DATE_FORMAT.format(new Date())),
                lines);

        String archiveSystemId = archivalService.archive(output);

        ArchivedRecord archivedRecord = new ArchivedRecord();
        archivedRecord.setDataSetName(dataSetName);
        archivedRecord.setStartDate(startDate);
        archivedRecord.setEndDate(endDate);
        archivedRecord.setArchiveSystemId(archiveSystemId);
        archivedRecord.setScriptType(scriptType);
        archivedRecordDao.saveArchivedRecord(archivedRecord);

        return archivedRecord;
    }

    @RequestMapping("/retrieveDataSet/{archiveSystemId}")
    @ResponseBody
    public File retrieveDataSet(@PathVariable String archiveSystemId) throws Exception {
        File retrievedFile = archivalService.retrieve(archiveSystemId);
        List<String> statements = fileSystemService.getLines(retrievedFile);
        dataExtractionService.loadData(jdbc, statements);
        return retrievedFile;
    }

    @RequestMapping("/getDataSet")
    @ResponseBody
    public List<DataSet> getDataSets() {
        return dataSetDao.getDataSets();
    }

    @RequestMapping("/getArchivedRecords")
    @ResponseBody
    public List<ArchivedRecord> getArchivedRecords() {
        return archivedRecordDao.getArchiveRecords();
    }
}
