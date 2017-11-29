package com.cg.cdars.service;

import com.cg.cdars.model.ScriptType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Date;
import java.util.List;

public interface DataExtractionService {
    List<String> generateSqlStatementsForDataSet(NamedParameterJdbcTemplate jdbc,
                                                 String dataSetName,
                                                 Date startDate,
                                                 Date endDate,
                                                 ScriptType scriptType) throws Exception;

    List<String> generateSqlStatementsForTable(NamedParameterJdbcTemplate jdbc,
                                               String tableName,
                                               Date startDate,
                                               Date endDate,
                                               ScriptType scriptType) throws Exception;

    void loadData(NamedParameterJdbcTemplate jdbc, List<String> statements);
}
