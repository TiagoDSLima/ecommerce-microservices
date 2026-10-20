package com.ecommerce.pedido.client.config;

import com.ecommerce.pedido.client.auth.ServiceTokenProvider;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

public class ServiceTokenFeignConfig {

    @Bean
    RequestInterceptor serviceTokenInterceptor(ServiceTokenProvider serviceTokenProvider) {
        return template -> template.header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceTokenProvider.getToken());
    }
}
