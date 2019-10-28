package com.platform.airportcloudapply;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.platform.airportcloudapply.mapper"})
@EnableTransactionManagement
public class AirportCloudApplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirportCloudApplyApplication.class, args);
    }

}
