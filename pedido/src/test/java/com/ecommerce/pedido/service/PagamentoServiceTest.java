package com.ecommerce.pedido.service;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.dto.PagamentoResponse;
import com.ecommerce.pedido.enums.BandeiraCartao;
import com.ecommerce.pedido.enums.StatusPagamento;
import com.ecommerce.pedido.enums.TipoPagamento;
import com.ecommerce.pedido.model.ItemPedido;
import com.ecommerce.pedido.model.PagamentoPedido;
import com.ecommerce.pedido.model.Pedido;
import com.ecommerce.pedido.publisher.ProdutoPublisher;
import com.ecommerce.pedido.repository.PagamentoPedidoRepository;
import com.ecommerce.pedido.strategy.pagamento.factory.PagamentoStrategyFactory;
import com.ecommerce.pedido.strategy.pagamento.impl.CreditoStrategy;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.resources.payment.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Testes unitarios do PagamentoService com o SDK do Mercado Pago mockado.
 */
@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoStrategyFactory factory;
    @Mock
    private MercadoPagoService mercadoPagoService;
    @Mock
    private PagamentoPedidoRepository pagamentoPedidoRepository;
    @Mock
    private ProdutoPublisher produtoPublisher;

    @InjectMocks
    private PagamentoService pagamentoService;

    private PagamentoPedido pagamentoPedido;

    @BeforeEach
    void setUp() {
        Pedido pedido = new Pedido();
        pedido.setId(42L);
        ItemPedido item = new ItemPedido();
        item.setIdProduto(1L);
        item.setQuantidade(2);
        pedido.setItensPedido(List.of(item));

        pagamentoPedido = new PagamentoPedido();
        pagamentoPedido.setStatusPagamento(StatusPagamento.PENDENTE);
        pagamentoPedido.setPedido(pedido);
    }

    private DadosPagamentoDto dadosCredito() {
        return new DadosPagamentoDto(42L, new BigDecimal("100.00"), "tok", TipoPagamento.CREDITO,
                BandeiraCartao.VISA, 1, "cliente@email.com", "12345678901", "Maria", "Silva");
    }

    private Payment payment(String status) {
        Payment payment = mock(Payment.class);
        lenient().when(payment.getId()).thenReturn(999L);
        lenient().when(payment.getStatus()).thenReturn(status);
        lenient().when(payment.getExternalReference()).thenReturn("42");
        return payment;
    }

    @Test
    void geraPagamentoCartaoAprovadoGravaStatusEPublicaProdutos() {
        Payment payment = payment("approved");
        when(pagamentoPedidoRepository.findByPedido_Id(42L)).thenReturn(pagamentoPedido);
        when(factory.getStrategy(TipoPagamento.CREDITO)).thenReturn(new CreditoStrategy());
        when(mercadoPagoService.criarPagamento(any(PaymentCreateRequest.class))).thenReturn(payment);

        PagamentoResponse response = pagamentoService.geraPagamento(dadosCredito());

        // Cartão não tem point_of_interaction: não pode estourar NPE
        assertThat(response.qrCodePix()).isNull();
        assertThat(response.statusPagamento()).isEqualTo(StatusPagamento.APROVADO);
        assertThat(pagamentoPedido.getMercadoPagoId()).isEqualTo(999L);
        verify(produtoPublisher).publicar(anyList());
        verify(pagamentoPedidoRepository).save(pagamentoPedido);
    }

    @Test
    void geraPagamentoRejeitadoNaoPublicaProdutos() {
        Payment payment = payment("rejected");
        when(pagamentoPedidoRepository.findByPedido_Id(42L)).thenReturn(pagamentoPedido);
        when(factory.getStrategy(TipoPagamento.CREDITO)).thenReturn(new CreditoStrategy());
        when(mercadoPagoService.criarPagamento(any(PaymentCreateRequest.class))).thenReturn(payment);

        PagamentoResponse response = pagamentoService.geraPagamento(dadosCredito());

        assertThat(response.statusPagamento()).isEqualTo(StatusPagamento.REJEITADO);
        verifyNoInteractions(produtoPublisher);
    }

    @Test
    void notificacaoDeAprovacaoPublicaProdutosUmaUnicaVez() {
        Payment payment = payment("approved");
        when(mercadoPagoService.buscarPagamento(999L)).thenReturn(payment);
        when(pagamentoPedidoRepository.findByMercadoPagoId(999L)).thenReturn(Optional.of(pagamentoPedido));

        pagamentoService.processarNotificacao(999L);
        pagamentoService.processarNotificacao(999L);

        assertThat(pagamentoPedido.getStatusPagamento()).isEqualTo(StatusPagamento.APROVADO);
        verify(produtoPublisher, times(1)).publicar(anyList());
    }

    @Test
    void notificacaoUsaExternalReferenceQuandoMercadoPagoIdAindaNaoFoiGravado() {
        Payment payment = payment("pending");
        when(mercadoPagoService.buscarPagamento(999L)).thenReturn(payment);
        when(pagamentoPedidoRepository.findByMercadoPagoId(999L)).thenReturn(Optional.empty());
        when(pagamentoPedidoRepository.findByPedido_Id(42L)).thenReturn(pagamentoPedido);

        pagamentoService.processarNotificacao(999L);

        assertThat(pagamentoPedido.getMercadoPagoId()).isEqualTo(999L);
        verify(pagamentoPedidoRepository).save(pagamentoPedido);
    }

    @Test
    void notificacaoDePagamentoDesconhecidoEhIgnorada() {
        Payment payment = payment("approved");
        when(payment.getExternalReference()).thenReturn(null);
        when(mercadoPagoService.buscarPagamento(999L)).thenReturn(payment);
        when(pagamentoPedidoRepository.findByMercadoPagoId(999L)).thenReturn(Optional.empty());

        pagamentoService.processarNotificacao(999L);

        verify(pagamentoPedidoRepository, never()).save(any());
        verifyNoInteractions(produtoPublisher);
    }
}
