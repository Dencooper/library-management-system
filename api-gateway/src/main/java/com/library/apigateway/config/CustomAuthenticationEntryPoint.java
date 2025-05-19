package com.library.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
//        ApiResponse<Object> res = new ApiResponse<>();
//        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
//        res.setError("Unauthenticated");
//        res.setMessage("Token không hợp lệ (hết hạn, không đúng định dạng, hoặc không truyền JWT ở header)");
        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().toString());
        errorBody.put("status", HttpStatus.UNAUTHORIZED.value());
        errorBody.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorBody.put("message", "Token không hợp lệ (hết hạn, không đúng định dạng, hoặc không truyền JWT ở header)");
        errorBody.put("path", exchange.getRequest().getURI().getPath());
        try {
            byte[] bytes = mapper.writeValueAsBytes(errorBody);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

}