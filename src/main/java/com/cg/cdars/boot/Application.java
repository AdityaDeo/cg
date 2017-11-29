package com.cg.cdars.boot;

import com.cg.cdars.controller.CdarsController;
import com.cg.cdars.dao.DaoConfig;
import com.cg.cdars.service.ServicesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {DaoConfig.class, ServicesConfig.class, CdarsController.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        System.out.println("Started Application");
    }
}
