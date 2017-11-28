package com.cg.cdars.service;

import com.cg.cdars.model.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Component
public class DummyClient {
    @Autowired
    private DataExtractionService dataExtractionService;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @PostConstruct
    public void testDataExtractionService() throws Exception {
        List<String> l = dataExtractionService.generateSqlStatementsForTable(jdbc,
                "trades",
                simpleDateFormat.parse("01-11-2017"),
                simpleDateFormat.parse("28-11-2017"),
                ScriptType.DDL);

        String str = l.stream().collect(joining(";\n"));

        int i = 1;
    }
}
