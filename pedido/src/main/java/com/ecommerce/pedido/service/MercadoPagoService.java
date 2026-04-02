package com.ecommerce.pedido.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    private final PaymentClient paymentClient;
    private final PagamentoService pagamentoService;

    public Payment criarPagamento(PaymentCreateRequest request) {

        MercadoPagoConfig.setAccessToken(getAcessToken());

        try {
            return paymentClient.create(request);
        } catch (Exception e){
            throw new RuntimeException("Erro ao criar pagamento ", e);
        }
    }

    public void processar(Map<String, Object> dados) {

        String type = (String) dados.get("type");

        if (!"payment".equals(type)) return;

        Map<String, Object> data = (Map<String, Object>) dados.get("data");
        String idStr = (String) data.get("id");

        Long paymentId = Long.valueOf(idStr);

        try {
            Payment payment = paymentClient.get(paymentId);

            pagamentoService.atualizarPagamento(payment);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar pagamento", e);
        }
    }



    private String getAcessToken(){
        //colocar para chaamr o microsserviço de loja para buscar token de acesso.
        return "";
    }
}
