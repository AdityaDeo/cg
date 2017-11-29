package com.cg.cdars.service;

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
    private NamedParameterJdbcTemplate jdbc;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @PostConstruct
    public void m1() throws Exception {
        List<String> lines = dataExtractionService.generateSqlStatementsForDataSet(jdbc,
                "DataSet1",
                simpleDateFormat.parse("01-11-2017"),
                simpleDateFormat.parse("28-11-2017"),
                ScriptType.DDL);

        File output = fileSystemService.storeToFile(String.format(FILE_NAME_PREFIX_FORMAT, SIMPLE_DATE_FORMAT.format(new Date())),
                lines);

        archivalService.archive(output);
    }


    public void m2() throws IOException {
        List<String> statements = fileSystemService.getLines(new File("C:\\Users\\Aditya\\AppData\\Local\\Temp\\DataSetArchive_29-11-2017_12_50_50_6070636350433757888.sql"));
        dataExtractionService.loadData(jdbc, statements);
        int i = 1;
    }
}
