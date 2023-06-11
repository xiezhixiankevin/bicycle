package com.bicycle.gateway.interceptor;

import cn.itcast.feign.util.JWTUtils;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.micrometer.core.instrument.util.StringUtils;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Component
public class TokenInterceptor implements GlobalFilter {

    private List<String> excludePathList = new ArrayList<>(20);

    public TokenInterceptor() {
        excludePathList.add("/user/login");
        excludePathList.add("/user/register");
        excludePathList.add("/user/get-register-code");
    }

    /**
     * 错误信息响应到客户端
     * @param serverHttpResponse Response
     * @date: 2021/4/20 9:13
     * @return: reactor.core.publisher.Mono<java.lang.Void>
     */
    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, Object info) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(info).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

    private boolean needExclude(String path){
        for (String s : excludePathList) {
            if (s.equals(path))
                return true;
        }
        return false;
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();

        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }

    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("在token检查与解析 被拦截了");
        Map<String,Object> map = new HashMap<>();
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethodValue();

        //排除路径
        String pathString = serverHttpRequest.getPath().toString();
        if (needExclude(pathString)){
            return chain.filter(exchange);
        }


        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        // 获取 token 参数
        String token = serverHttpRequest.getHeaders().getFirst("token");

        if (StringUtils.isBlank(token)) {
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            map.put("message","无token！");
            map.put("success",false);  // 设置状态
            // 将map以json的形式响应到前台  map --> json  (jackson)
            return getVoidMono(serverHttpResponse, map);
        }

        System.out.println(token);
        try {
            // 验证令牌
            DecodedJWT verify = JWTUtils.verify(token);
        } catch (SignatureVerificationException e) {
            map.put("message","无效签名！");
            map.put("success",false);  // 设置状态
            return getVoidMono(serverHttpResponse, map);
        }catch (TokenExpiredException e){
            map.put("message","token过期");
            map.put("success",false);  // 设置状态
            return getVoidMono(serverHttpResponse, map);
        }catch (AlgorithmMismatchException e){
            map.put("message","算法不一致");
            map.put("success",false);  // 设置状态
            return getVoidMono(serverHttpResponse, map);
        }catch (Exception e){
            map.put("message","token无效！");
            map.put("success",false);  // 设置状态
            return getVoidMono(serverHttpResponse, map);
        }
        return chain.filter(exchange);
    }

}
