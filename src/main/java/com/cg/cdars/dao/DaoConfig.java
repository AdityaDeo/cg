package com.cg.cdars.dao;

import com.cg.cdars.dao.impl.BeanDaoImpl;
import com.cg.cdars.dao.impl.TableInformationDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:datasource.properties")
public class DaoConfig {
    @Value("${db.driverClassName}")
    private String driverClassName;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Bean
    public DataSource getDefaultDatasource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean
    @Autowired
    public NamedParameterJdbcTemplate getDefaultNamedParameterJdbcTemplate(@Qualifier("getDefaultDatasource") DataSource ds) {
        return new NamedParameterJdbcTemplate(ds);
    }

    @Bean
    @Autowired
    public TableInformationDao getTableInformationDao(NamedParameterJdbcTemplate jdbc) {
        TableInformationDaoImpl dao = new TableInformationDaoImpl();
        dao.setJdbc(jdbc);
        return dao;
    }
}
