package com.ecommerce.pedido.controller;

import com.ecommerce.pedido.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook/mercadopago")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {

    private final PagamentoService pagamentoService;

    /**
     * O Mercado Pago envia o tipo e o id tanto no corpo ({"type":"payment","data":{"id":"123"}})
     * quanto na query string (?type=payment&data.id=123). O id pode vir como texto ou número.
     */
    @PostMapping
    public ResponseEntity<Void> receberWebhook(@RequestBody(required = false) Map<String, Object> dados,
                                               @RequestParam Map<String, String> params) {

        String tipo = params.get("type");
        Object id = params.get("data.id");

        if (dados != null) {
            if (dados.get("type") != null) tipo = String.valueOf(dados.get("type"));
            if (dados.get("data") instanceof Map<?, ?> data && data.get("id") != null) id = data.get("id");
        }

        if (!"payment".equals(tipo) || id == null) {
            return ResponseEntity.ok().build();
        }

        pagamentoService.processarNotificacao(Long.valueOf(String.valueOf(id)));

        return ResponseEntity.ok().build();
    }

}
