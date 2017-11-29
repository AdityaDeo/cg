package com.cg.cdars.service;

import com.cg.cdars.dao.DataSetDao;
import com.cg.cdars.dao.TableInformationDao;
import com.cg.cdars.service.impl.ArchivalServiceImpl;
import com.cg.cdars.service.impl.DataExtractionServiceImpl;
import com.cg.cdars.service.impl.FileSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:datasource.properties")
public class ServicesConfig {

    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.scretKey}")
    private String awsSecretKey;

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

    @Bean
    public ArchivalService getArchivalService() {
        ArchivalServiceImpl service = new ArchivalServiceImpl();
        service.setAccessKey(awsAccessKey);
        service.setSecretKey(awsSecretKey);
        return service;
    }
}
