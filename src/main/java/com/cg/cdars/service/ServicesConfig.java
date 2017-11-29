package com.cg.cdars.service;

import com.cg.cdars.dao.DataSetDao;
import com.cg.cdars.dao.TableInformationDao;
import com.cg.cdars.service.impl.DataExtractionServiceImpl;
import com.cg.cdars.service.impl.FileSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    @Autowired
    public DataExtractionService getDataExtractionService(DataSetDao dataSetDao, TableInformationDao tableInformationDao) {
        DataExtractionServiceImpl service = new DataExtractionServiceImpl();
        service.setDataSetDao(dataSetDao);
        service.setTableInformationDao(tableInformationDao);
        return service;
    }

    @Bean
    public FileSystemService getFileSystemService() {
        FileSystemServiceImpl service = new FileSystemServiceImpl();
        return service;
    }
}
