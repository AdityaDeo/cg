package com.cg.cdars.service;

import com.cg.cdars.dao.ArchivedRecordDao;
import com.cg.cdars.model.ArchivedRecord;
import com.cg.cdars.model.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.cg.cdars.model.ScriptType.DDL;
import static java.lang.String.format;

@Component
public class DummyClient {
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
    private NamedParameterJdbcTemplate jdbc;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


    public void m1() throws Exception {
        String dataSetName = "DataSet1";
        Date startDate = simpleDateFormat.parse("01-11-2017");
        Date endDate = simpleDateFormat.parse("28-11-2017");
        ScriptType scriptType = DDL;

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
    }

    @PostConstruct
    public void m2() throws Exception {
        String key = "DataSetArchive/DataSetArchive_29-11-2017_14_05_17_8594223761495330563.sql";
        File retrievedFile = archivalService.retrieve("sample", key);
        List<String> statements = fileSystemService.getLines(retrievedFile);
        dataExtractionService.loadData(jdbc, statements);
    }
}
