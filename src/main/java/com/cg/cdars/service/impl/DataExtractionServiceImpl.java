package com.cg.cdars.service.impl;

import com.cg.cdars.dao.DataSetDao;
import com.cg.cdars.dao.TableInformationDao;
import com.cg.cdars.model.DataSet;
import com.cg.cdars.model.ScriptType;
import com.cg.cdars.model.SqlDataTypes;
import com.cg.cdars.service.DataExtractionService;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static com.cg.cdars.model.ScriptType.DDL;
import static com.cg.cdars.model.SqlDataTypes.*;
import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class DataExtractionServiceImpl implements DataExtractionService {
    private final Map<SqlDataTypes, Function<Object, String>> SQL_VALUE_LITERAL_GENERATORS = new EnumMap<>(SqlDataTypes.class);
    private final String DATE_PATTERN = "dd-MM-yyyy";
    private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_PATTERN);
    private TableInformationDao tableInformationDao;
    private DataSetDao dataSetDao;

    public DataExtractionServiceImpl() {
        SQL_VALUE_LITERAL_GENERATORS.put(VARCHAR2, o -> "'" + o + "'");
        SQL_VALUE_LITERAL_GENERATORS.put(NUMBER, o -> o + "");
        SQL_VALUE_LITERAL_GENERATORS.put(DATE, o -> "to_date('" + DATE_FORMATTER.format(o) + "', '"+ DATE_PATTERN + "')");
    }

    @Override
    public List<String> generateSqlStatementsForDataSet(NamedParameterJdbcTemplate jdbc,
                                                        String dataSetName,
                                                        Date startDate,
                                                        Date endDate,
                                                        ScriptType scriptType) throws Exception {

        DataSet dataSet = dataSetDao.getDataSets().stream()
                .filter(ds -> ds.getDatasetName().equals(dataSetName))
                .findFirst().get();

        return dataSet.getTables().stream()
                .map(table -> {
                    try {
                        return generateSqlStatementsForTable(jdbc, table, startDate, endDate, DDL);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Collections.<String>emptyList();
                    }})
                .flatMap(List::stream)
                .collect(toList());
    }

    @Override
    public List<String> generateSqlStatementsForTable(NamedParameterJdbcTemplate jdbc,
                                                      String tableName,
                                                      Date startDate,
                                                      Date endDate,
                                                      ScriptType scriptType) throws Exception {
        List<String> result = new LinkedList<>();
        if (scriptType == DDL) {
            result.add(getCreateStatementForTable(tableName));
        }
        result.addAll(getInsertStatementsForTable(jdbc, tableName, startDate, endDate));
        return result;
    }

    private String getCreateStatementForTable(String tableName) {
        Map<String, String> tableDefinition = tableInformationDao.getTableDefinition(tableName);
        return new StringBuilder()
                .append("create table ")
                .append(tableName)
                .append(" (")
                .append(tableDefinition.entrySet().stream().sorted(comparing(Entry::getKey)).map(e -> e.getKey() + " " + e.getValue()).collect(joining(", ")))
                .append(")")
                .toString();
    }

    private List<String> getInsertStatementsForTable(NamedParameterJdbcTemplate jdbc,
                                                     String tableName,
                                                     Date startDate,
                                                     Date endDate) {
        Map<String, SqlDataTypes> columns = tableInformationDao.getTableColumns(tableName);
        String dateColumnName = tableInformationDao.getDateColumnName(tableName);
        String template = getInsertStatementTemplate(tableName, new ArrayList<>(columns.keySet()));
        List<Map<String, Object>> rows = getRowsAsMaps(jdbc, tableName, dateColumnName, startDate, endDate);
        return rows.stream().map(m -> format(template, getInsertStatementValuesForRow(columns, m))).collect(toList());
    }


    private String getInsertStatementTemplate(String tableName, List<String> columnNames) {
        return new StringBuilder()
                .append("insert into ")
                .append(tableName)
                .append(" (")
                .append(columnNames.stream().sorted().collect(joining(", ")))
                .append(") values (%s)")
                .toString();
    }

    private List<Map<String, Object>> getRowsAsMaps(NamedParameterJdbcTemplate jdbc,
                                                    String tableName,
                                                    String dateColumnName,
                                                    Date startDate,
                                                    Date endDate) {
        StringBuilder sb = new StringBuilder()
            .append(" select * from ")
            .append(tableName);

        if (dateColumnName != null) {
            sb.append(" where ")
                    .append(dateColumnName)
                    .append(" <= :endDate")
                    .append(" and ")
                    .append(dateColumnName)
                    .append(" >= :startDate ");
        }

        Map<String, Date> params = new HashMap();
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        return jdbc.query(sb.toString(), params, new ColumnMapRowMapper());
    }

    private String getInsertStatementValuesForRow(Map<String, SqlDataTypes> columnDefinitions, Map<String, Object> row) {
        return row.entrySet().stream().sorted(comparing(Entry::getKey))
                    .map(e -> SQL_VALUE_LITERAL_GENERATORS.get(columnDefinitions.get(e.getKey())).apply(e.getValue()))
                    .collect(joining(", "));
    }

    @Override
    public void loadData(NamedParameterJdbcTemplate jdbc, List<String> statements) {

    }

    public void setTableInformationDao(TableInformationDao tableInformationDao) {
        this.tableInformationDao = tableInformationDao;
    }

    public void setDataSetDao(DataSetDao dataSetDao) {
        this.dataSetDao = dataSetDao;
    }
}
