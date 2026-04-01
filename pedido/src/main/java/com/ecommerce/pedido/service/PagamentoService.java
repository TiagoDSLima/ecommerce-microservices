package com.ecommerce.pedido.service;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.enums.StatusPagamento;
import com.ecommerce.pedido.model.PagamentoPedido;
import com.ecommerce.pedido.repository.PagamentoPedidoRepository;
import com.ecommerce.pedido.strategy.pagamento.factory.PagamentoStrategyFactory;
import com.ecommerce.pedido.strategy.pagamento.strategy.PagamentoStrategy;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoStrategyFactory factory;
    private final MercadoPagoService mercadoPagoService;
    private final PagamentoPedidoRepository pagamentoPedidoRepository;

    public Payment geraPagamento(DadosPagamentoDto dadosPagamento){

        PagamentoStrategy strategy = factory.getStrategy(dadosPagamento.tipo());
        PaymentCreateRequest request = strategy.criar(dadosPagamento);

        Payment payment = mercadoPagoService.criarPagamento(request);
        PagamentoPedido pagamentoPedido = pagamentoPedidoRepository.findByPedido_Id(dadosPagamento.pedidoId());
        StatusPagamento statusPagamento = StatusPagamento.PENDENTE;

        return null;
    }
}
