package com.cg.cdars.dao.impl;

import com.cg.cdars.dao.BeanDao;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class BeanDaoImpl implements BeanDao {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private Map<String, String> queryMap;

    @Override
    public <T> T getObject(String sqlId, Class<T> clazz) {
        return getObject(sqlId, emptyMap(), clazz);
    }

    @Override
    public <T> T getObject(String sqlId, Map<String, ?> parameters, Class<T> clazz) {
        return namedParameterJdbcTemplate.queryForObject(getSqlText(sqlId), parameters, clazz);
    }

    @Override
    public <T> List<T> getList(String sqlId, Class<T> clazz) {
        return getList(sqlId, emptyMap(), clazz);
    }

    @Override
    public <T> List<T> getList(String sqlId, Map<String, ?> parameters, Class<T> clazz) {
        return namedParameterJdbcTemplate.queryForList(getSqlText(sqlId), parameters, clazz);
    }

    @Override
    public <T> int saveObject(String sqlId, T object) {
        return namedParameterJdbcTemplate.update(getSqlText(sqlId), new BeanPropertySqlParameterSource(object));
    }

    @Override
    public <T> int[] saveList(String sqlId, List<T> list) {
        BeanPropertySqlParameterSource[] params = list.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(toList())
                .toArray(new BeanPropertySqlParameterSource[] {});
        return namedParameterJdbcTemplate.batchUpdate(getSqlText(sqlId), params);
    }

    @Override
    public void execute(String sqlId) {
        execute(sqlId, emptyMap());
    }

    @Override
    public void execute(String sqlId, Map<String, ?> parameters) {
        namedParameterJdbcTemplate.update(getSqlText(sqlId), parameters);
    }

    private String getSqlText(String sqlId) {
        requireNonNull(queryMap, "Query map not set");
        String sqlText = queryMap.get(sqlId);
        requireNonNull(sqlId, format("Sql with id %s not found", sqlId));
        return sqlText;
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = unmodifiableMap(queryMap);
    }
}
