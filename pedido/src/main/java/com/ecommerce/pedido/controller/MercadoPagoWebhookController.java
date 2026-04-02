package com.ecommerce.pedido.controller;

import com.ecommerce.pedido.service.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/webhook/mercadopago")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {

    private final MercadoPagoService webhookService;

    @PostMapping
    public ResponseEntity<Void> receberWebhook(@RequestBody Map<String, Object> dados) {

        webhookService.processar(dados);

        return ResponseEntity.ok().build();
    }

}
