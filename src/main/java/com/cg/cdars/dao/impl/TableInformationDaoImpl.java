package com.cg.cdars.dao.impl;

import com.cg.cdars.dao.TableInformationDao;
import com.cg.cdars.model.SqlDataTypes;
import com.cg.cdars.model.TableInformation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toMap;

public class TableInformationDaoImpl implements TableInformationDao {
    private NamedParameterJdbcTemplate jdbc;

    public List<TableInformation> getTableInformation() {
        List<TableInformation> tableInformation = jdbc.query("select * from tableinformation where key = 'datecolumn'", new BeanPropertyRowMapper<>(TableInformation.class));
        return tableInformation;
    }

    public String getDateColumnName(String tableName) {
        try {
            return jdbc.queryForObject("select value from tableinformation where key = 'datecolumn' and lower(tableName) = lower(:tableName)",
                    singletonMap("tableName", tableName),
                    String.class);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Map<String, SqlDataTypes> getTableColumns(String tableName) {
        List<ImmutablePair<String, SqlDataTypes>> pairs = jdbc.query("select column_name, data_type from all_tab_columns where lower(table_name) = lower(:tableName)",
                singletonMap("tableName", tableName),
                (resultSet, i) -> new ImmutablePair<>(resultSet.getString(1), SqlDataTypes.valueOf(resultSet.getString(2))));

        return pairs.stream() .collect(toMap(ImmutablePair::getLeft, ImmutablePair::getRight));
    }

    @Override
    public Map<String, String> getTableDefinition(String tableName) {
        List<ImmutablePair<String, String>> pairs = jdbc.query("select column_name, data_type || case data_type when 'VARCHAR2' then '(' || data_length || ')' else '' end data_type from all_tab_columns where lower(table_name) = lower(:tableName)",
                singletonMap("tableName", tableName),
                (resultSet, i) -> new ImmutablePair<>(resultSet.getString(1), resultSet.getString(2)));

        return pairs.stream() .collect(toMap(ImmutablePair::getLeft, ImmutablePair::getRight));
    }

    public void setJdbc(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
}
