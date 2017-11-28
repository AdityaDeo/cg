package com.cg.cdars.dao.impl;

import com.cg.cdars.dao.DataSetDao;
import com.cg.cdars.model.DataSet;
import com.cg.cdars.model.DataSetType;
import javafx.util.Pair;
import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class DataSetDaoImpl implements DataSetDao {
    private NamedParameterJdbcTemplate jdbc;

    private RowMapper pairRowMapper = (resultSet, i) -> new Pair<>(resultSet.getString(1), resultSet.getString(2));

    public List<DataSet> getDataSets() {
        List<Pair<String, String>> dataSets = jdbc.query("select * from datasets", pairRowMapper);
        List<Pair<String, String>> dataSetsXTables = jdbc.query("select * from datasetXtables", pairRowMapper);
        return pairsToDataSet(dataSets, dataSetsXTables);
    }

    private List<DataSet> pairsToDataSet(List<Pair<String, String>> dataSets,
                                         List<Pair<String, String>> dataSetsXTables) {

        Map<String, List<String>> dxt = dataSetsXTables.stream()
                .collect(toMap(p -> p.getKey(), p -> singletonList(p.getValue()), ListUtils::union));

        return dataSets.stream()
                .map(p -> new DataSet(p.getKey(), DataSetType.valueOf(p.getValue()), dxt.get(p.getKey())))
                .collect(toList());
    }

    public void setJdbc(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
}
