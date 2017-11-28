package com.cg.cdars.dao;

import java.util.List;
import java.util.Map;

public interface BeanDao {
    <T> T getObject(String sqlId, Class<T> clazz);
    <T> T getObject(String sqlId, Map<String, ?> parameters, Class<T> clazz);
    <T> List<T> getList(String sqlId, Class<T> clazz);
    <T> List<T> getList(String sqlId, Map<String, ?> parameters, Class<T> clazz);
    <T> int saveObject(String sqlId, T object);
    <T> int[] saveList(String sqlId, List<T> list);
    void execute(String sqlId);
    void execute(String sqlId, Map<String, ?> parameters);
}
