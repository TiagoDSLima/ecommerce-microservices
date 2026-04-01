package com.ecommerce.pedido.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;

@Service
public class MercadoPagoService {

    private final PaymentClient paymentClient = new PaymentClient();

    public Payment criarPagamento(PaymentCreateRequest request) {

        MercadoPagoConfig.setAccessToken(getAcessToken());

        try {
            return paymentClient.create(request);
        } catch (Exception e){
            throw new RuntimeException("Erro ao criar pagamento ", e);
        }
    }

    private String getAcessToken(){
        //colocar para chaamr o microsserviço de loja para buscar token de acesso.
        return "";
    }
}
