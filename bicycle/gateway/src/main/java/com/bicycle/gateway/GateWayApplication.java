package com.bicycle.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <Description> GateWayApplication
 *
 * @author 26802
 * @version 1.0
 * @see com.bicycle.gateway
 */

@EnableDiscoveryClient // 启动服务注册与发现
@SpringBootApplication
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class);
    }
}
