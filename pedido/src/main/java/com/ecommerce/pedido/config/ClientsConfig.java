package com.ecommerce.pedido.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.ecommerce.pedido.client")
public class ClientsConfig {
}
