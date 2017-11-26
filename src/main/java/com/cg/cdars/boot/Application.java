package com.cg.cdars.boot;

import com.cg.cdars.dao.GenericDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {GenericDao.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
