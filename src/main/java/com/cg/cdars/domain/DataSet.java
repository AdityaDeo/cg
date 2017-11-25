package com.cg.cdars.domain;

import java.util.List;

public class DataSet {
    private String id;
    private String datasetName;
    private List<String> tableNames;
    private DataSetType dataSetType;

    public enum DataSetType {
        TRADE, AGREEMENT, PRODUCT, ALL
    }
}
