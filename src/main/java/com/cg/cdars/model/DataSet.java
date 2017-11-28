package com.cg.cdars.model;

import java.util.List;

public class DataSet {
    private String datasetName;
    private DataSetType dataSetType;
    private List<String> tables;

    public DataSet(String datasetName, DataSetType dataSetType, List<String> tables) {
        this.datasetName = datasetName;
        this.dataSetType = dataSetType;
        this.tables = tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public DataSetType getDataSetType() {
        return dataSetType;
    }

    public List<String> getTables() {
        return tables;
    }
}
