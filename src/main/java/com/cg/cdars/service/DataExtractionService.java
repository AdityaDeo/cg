package com.cg.cdars.service;

import com.cg.cdars.dao.GenericDao;
import com.cg.cdars.domain.DataSet.DataSetType;
import com.cg.cdars.domain.ScriptType;

import java.util.Date;
import java.util.List;

public interface DataExtractionService {
    void setConfigurationDao(GenericDao configurationDao);

    List<String> generateSqlStatementsForDataSet(GenericDao dao,
                                                 DataSetType dataSetName,
                                                 Date startDate,
                                                 Date endDate,
                                                 ScriptType scriptType) throws Exception;

    List<String> generateSqlStatementsForTable(GenericDao dao,
                                               String tableName,
                                               String dateColumnName,
                                               Date startDate,
                                               Date endDate,
                                               ScriptType scriptType) throws Exception;

    void loadData(GenericDao genericDao, List<String> statements);
}
