package com.cg.cdars.dao.impl;

import com.cg.cdars.dao.ArchivedRecordDao;
import com.cg.cdars.model.ArchivedRecord;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static java.sql.Types.VARCHAR;

public class ArchivedRecordDaoImpl implements ArchivedRecordDao {
    private NamedParameterJdbcTemplate jdbc;

    public List<ArchivedRecord> getArchiveRecords() {
        List<ArchivedRecord> result = jdbc.query("select * from archivedrecords", new BeanPropertyRowMapper<>(ArchivedRecord.class));
        return result;
    }

    public int saveArchivedRecord(ArchivedRecord record) {
        BeanPropertySqlParameterSource src = new BeanPropertySqlParameterSource(record);
        src.registerSqlType("scriptType", VARCHAR);
        return jdbc.update("insert into archivedrecords (datasetname, startdate , enddate , archivesystemid, filefullname,  createdby, creationtime, scriptType) " +
                        " values (:dataSetName, :startDate, :endDate, :archiveSystemId, :fileFullName, :createdBy, :creationTime, :scriptType)",
                src);
    }

    public void setJdbc(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
}
