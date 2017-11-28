package com.cg.cdars.dao;

import com.cg.cdars.model.SqlDataTypes;
import com.cg.cdars.model.TableInformation;

import java.util.List;
import java.util.Map;

public interface TableInformationDao {
    List<TableInformation> getTableInformation();
    String getDateColumnName(String tableName);
    Map<String, SqlDataTypes> getTableColumns(String tableName);
    Map<String, String> getTableDefinition(String tableName);
}
