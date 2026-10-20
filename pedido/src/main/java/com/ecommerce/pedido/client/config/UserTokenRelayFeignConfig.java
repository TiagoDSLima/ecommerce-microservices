package com.ecommerce.pedido.client.config;

import com.ecommerce.pedido.client.auth.ServiceTokenProvider;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UserTokenRelayFeignConfig {

    @Bean
    RequestInterceptor userTokenRelayInterceptor(ServiceTokenProvider serviceTokenProvider) {
        return template -> {
            String authorization = null;
            if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
                authorization = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            }
            template.header(HttpHeaders.AUTHORIZATION,
                    authorization != null ? authorization : "Bearer " + serviceTokenProvider.getToken());
        };
    }
}
