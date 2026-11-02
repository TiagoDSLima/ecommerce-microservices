package com.ecommerce.pedido.service;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.dto.PagamentoResponse;
import com.ecommerce.pedido.enums.StatusPagamento;
import com.ecommerce.pedido.exception.exceptions.PagamentoException;
import com.ecommerce.pedido.model.PagamentoPedido;
import com.ecommerce.pedido.model.Pedido;
import com.ecommerce.pedido.publisher.ProdutoPublisher;
import com.ecommerce.pedido.publisher.representation.ProdutoRepresentation;
import com.ecommerce.pedido.repository.PagamentoPedidoRepository;
import com.ecommerce.pedido.strategy.pagamento.factory.PagamentoStrategyFactory;
import com.ecommerce.pedido.strategy.pagamento.strategy.PagamentoStrategy;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentTransactionData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoStrategyFactory factory;
    private final MercadoPagoService mercadoPagoService;
    private final PagamentoPedidoRepository pagamentoPedidoRepository;
    private final ProdutoPublisher produtoPublisher;

    @Transactional
    public PagamentoResponse geraPagamento(DadosPagamentoDto dadosPagamento){

        PagamentoPedido pagamentoPedido = pagamentoPedidoRepository.findByPedido_Id(dadosPagamento.pedidoId());
        if (pagamentoPedido == null) {
            throw new PagamentoException("pagamento", String.format("Pagamento do pedido %d não encontrado", dadosPagamento.pedidoId()));
        }

        PagamentoStrategy strategy = factory.getStrategy(dadosPagamento.tipo());
        PaymentCreateRequest request = strategy.criar(dadosPagamento);

        Payment payment = mercadoPagoService.criarPagamento(request);

        pagamentoPedido.setMercadoPagoId(payment.getId());
        aplicaStatus(pagamentoPedido, payment);
        pagamentoPedidoRepository.save(pagamentoPedido);

        return montaRetorno(payment, dadosPagamento);
    }

    @Transactional
    public void processarNotificacao(Long paymentId){
        Payment payment = mercadoPagoService.buscarPagamento(paymentId);

        Optional<PagamentoPedido> pagamentoOpt = pagamentoPedidoRepository.findByMercadoPagoId(payment.getId());
        if (pagamentoOpt.isEmpty() && payment.getExternalReference() != null) {
            // A notificação pode chegar antes do mercadoPagoId ser gravado; o external_reference é o id do pedido
            pagamentoOpt = Optional.ofNullable(pagamentoPedidoRepository.findByPedido_Id(Long.valueOf(payment.getExternalReference())));
        }

        if (pagamentoOpt.isEmpty()) {
            log.warn("Notificação do Mercado Pago ignorada: pagamento {} não pertence a nenhum pedido", paymentId);
            return;
        }

        PagamentoPedido pagamento = pagamentoOpt.get();
        pagamento.setMercadoPagoId(payment.getId());
        aplicaStatus(pagamento, payment);
        pagamentoPedidoRepository.save(pagamento);
    }

    private void aplicaStatus(PagamentoPedido pagamento, Payment payment){
        StatusPagamento novoStatus = retornaStatus(payment.getStatus());

        if(!StatusPagamento.APROVADO.equals(pagamento.getStatusPagamento())
            && StatusPagamento.APROVADO.equals(novoStatus)) {
            publicaProdutosVendidos(pagamento.getPedido());
        }
        pagamento.setStatusPagamento(novoStatus);
        pagamento.setObservacaoStatus(payment.getStatusDetail());
    }

    private void publicaProdutosVendidos(Pedido pedido){
        List<ProdutoRepresentation> produtos = pedido.getItensPedido().stream()
                .map(itemPedido -> new ProdutoRepresentation(itemPedido.getIdProduto(), itemPedido.getIdProdutoVariacao(), itemPedido.getQuantidade()))
                .toList();
        produtoPublisher.publicar(produtos);
    }

    private PagamentoResponse montaRetorno(Payment payment, DadosPagamentoDto dadosPagamento){
        // point_of_interaction só vem preenchido no Pix; em cartão é nulo
        PaymentTransactionData transacao = payment.getPointOfInteraction() != null
                ? payment.getPointOfInteraction().getTransactionData()
                : null;

        return new PagamentoResponse(
                payment.getId(),
                dadosPagamento.tipo(),
                dadosPagamento.bandeira(),
                retornaStatus(payment.getStatus()),
                payment.getStatusDetail(),
                payment.getTransactionAmount(),
                transacao != null ? transacao.getQrCode() : null,
                transacao != null ? transacao.getQrCodeBase64() : null
        );
    }

    private StatusPagamento retornaStatus(String statusMercadoPago){
        if (statusMercadoPago == null) return StatusPagamento.PENDENTE;

        return switch (statusMercadoPago) {
            case "approved" -> StatusPagamento.APROVADO;
            case "authorized", "in_process", "in_mediation" -> StatusPagamento.PROCESSANDO;
            case "rejected" -> StatusPagamento.REJEITADO;
            case "cancelled" -> StatusPagamento.CANCELADO;
            case "refunded" -> StatusPagamento.DEVOLVIDO;
            case "charged_back" -> StatusPagamento.CONTESTADO;
            default -> StatusPagamento.PENDENTE;
        };
    }
}
