package com.zsy.loan.dao;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackages = "com.zsy.loan.bean.entity")
@EnableJpaRepositories(basePackages = "com.zsy.loan.dao.system")
public class AppConfiguration {

}

