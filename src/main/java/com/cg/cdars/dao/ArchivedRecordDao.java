package com.cg.cdars.dao;

import com.cg.cdars.model.ArchivedRecord;

import java.util.List;

public interface ArchivedRecordDao {
    List<ArchivedRecord> getArchiveRecords();
    int saveArchivedRecord(ArchivedRecord record);
}
