package com.ecommerce.pedido.service;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.dto.PagamentoResponse;
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

    public PagamentoResponse geraPagamento(DadosPagamentoDto dadosPagamento){

        PagamentoStrategy strategy = factory.getStrategy(dadosPagamento.tipo());
        PaymentCreateRequest request = strategy.criar(dadosPagamento);

        Payment payment = mercadoPagoService.criarPagamento(request);
        PagamentoPedido pagamentoPedido = pagamentoPedidoRepository.findByPedido_Id(dadosPagamento.pedidoId());
        PagamentoResponse response = montaRetorno(payment, dadosPagamento);
        pagamentoPedido.setStatusPagamento(response.statusPagamento());
        pagamentoPedido.setMercadoPagoId(response.mercadoPagoId());
        pagamentoPedido.setObservacaoStatus(response.observacaoStatus());
        pagamentoPedidoRepository.save(pagamentoPedido);

        return response;
    }

    public void atualizarPagamento(Payment payment){
        PagamentoPedido pagamento = pagamentoPedidoRepository
                .findByMercadoPagoId(payment.getId())
                .orElseThrow();

        //publicar produtos vendidos
        pagamento.setStatusPagamento(retornaStatus(payment.getStatus()));
        pagamento.setObservacaoStatus(payment.getStatusDetail());
        pagamentoPedidoRepository.save(pagamento);
    }

    private PagamentoResponse montaRetorno(Payment payment, DadosPagamentoDto dadosPagamento){
        var transacao = payment.getPointOfInteraction().getTransactionData();

        return new PagamentoResponse(
                payment.getId(),
                dadosPagamento.tipo(),
                dadosPagamento.bandeira(),
                retornaStatus(payment.getStatus()),
                payment.getStatusDetail(),
                payment.getTransactionAmount(),
                "pix".equals(payment.getPaymentMethodId()) ? transacao.getQrCode() : null,
                "pix".equals(payment.getPaymentMethodId()) ? transacao.getQrCodeBase64() : null
        );
    }

    private StatusPagamento retornaStatus(String statusMercadoPago){
        switch (statusMercadoPago) {
            case "approved" -> {
                return StatusPagamento.APROVADO;
            }
            case "pending" -> {
                return StatusPagamento.PENDENTE;
            }
            case "rejected" -> {
                return StatusPagamento.PROCESSANDO;
            }
            case "cancelled" -> {
                return StatusPagamento.CANCELADO;
            }
            case "refunded" -> {
                return StatusPagamento.DEVOLVIDO;
            }
            case "charged_back" -> {
                return StatusPagamento.CONTESTADO;
            }
        }

        return StatusPagamento.PENDENTE;
    }
}
