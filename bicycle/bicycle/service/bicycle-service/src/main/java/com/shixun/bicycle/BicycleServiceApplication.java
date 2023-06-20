package com.shixun.bicycle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.shixun.bicycle.mapper")
@EnableCircuitBreaker
@EnableSwagger2  //加入EnableSwagger2
@EnableDiscoveryClient
public class BicycleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BicycleServiceApplication.class, args);
    }

}
