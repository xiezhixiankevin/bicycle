package com.bicycle.gateway.config;

import com.bicycle.gateway.interceptor.TokenInterceptor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <Description> MyInterceptorConfig
 *
 * @author 26802
 * @version 1.0
 */
//@Configuration
public class MyInterceptorConfig {

//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes().route(r -> r
//                // 断言（判断条件）
//                .path("/product/**")
//                // 目标 URI，路由到微服务的地址
//                .uri("lb://ai-product")
//                // 注册自定义网关过滤器
//                .filters(new TokenInterceptor())
//                // 路由 ID，唯一
//                .id("product"))
//                .build();
//        return builder.routes().route(r ->
//                r.path("/user/list")
//                .uri("http://localhost:8077/api/user/list")
//                .filters(new AuthorizeGatewayFilter())
//                .id("user-service"))
//                .build();
//
//    }
//
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(new TokenInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/user/user/login", "/user/user/register", "/user/user/get-register-code")
//                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
//                .order(0);
//
////        super.addInterceptors(registry);
//    }
//
//    /**
//     * 解决swagger静态资源问题
//     */
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 解决swagger无法访问
//        registry.addResourceHandler("/swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        // 解决swagger的js文件无法访问
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//        // 解决静态资源无法访问
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/");
//    }


}

