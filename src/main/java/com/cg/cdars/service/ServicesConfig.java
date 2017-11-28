package com.cg.cdars.service;

import com.cg.cdars.dao.TableInformationDao;
import com.cg.cdars.service.impl.DataExtractionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    @Autowired
    public DataExtractionService getDataExtractionService(TableInformationDao dao) {
        DataExtractionServiceImpl service = new DataExtractionServiceImpl();
        service.setTableInformationDao(dao);
        return service;
    }
}
