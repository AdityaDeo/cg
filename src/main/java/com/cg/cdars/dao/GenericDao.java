package com.cg.cdars.dao;

import java.util.List;

public interface GenericDao {
    <T> T getObject(String sqlText, Class<T> clazz);
    <T> void saveObject(String sqlText, T object);
    <T> List<T> getList(String sqlText, Class<T> clazz);
    <T> void saveList(String sqlText, List<T> list);
    void execute(String sqlText);
}
