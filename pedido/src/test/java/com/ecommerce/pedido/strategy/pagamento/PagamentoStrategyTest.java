package com.ecommerce.pedido.strategy.pagamento;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.enums.BandeiraCartao;
import com.ecommerce.pedido.enums.TipoPagamento;
import com.ecommerce.pedido.strategy.pagamento.impl.CreditoStrategy;
import com.ecommerce.pedido.strategy.pagamento.impl.DebitoStrategy;
import com.ecommerce.pedido.strategy.pagamento.impl.PixStrategy;
import com.mercadopago.client.payment.PaymentCreateRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitarios puros das estrategias de pagamento.
 * Cada estrategia apenas monta um {@link PaymentCreateRequest} a partir do DTO,
 * entao nao ha integracao (Spring, banco, Kafka ou MercadoPago) envolvida.
 */
class PagamentoStrategyTest {

    /** Monta um DTO de pagamento variando apenas os campos relevantes para cada cenario. */
    private DadosPagamentoDto dados(TipoPagamento tipo, BandeiraCartao bandeira, Integer parcelas, String cpfCnpj) {
        return new DadosPagamentoDto(
                42L,
                new BigDecimal("100.00"),
                "tok_abc",
                tipo,
                bandeira,
                parcelas,
                "cliente@email.com",
                cpfCnpj,
                "Maria",
                "Silva"
        );
    }

    @Test
    void creditoStrategyDevePreencherParcelasEMetodoDePagamento() {
        DadosPagamentoDto dados = dados(TipoPagamento.CREDITO, BandeiraCartao.VISA, 3, "12345678901");

        PaymentCreateRequest request = new CreditoStrategy().criar(dados);

        assertThat(request.getInstallments()).isEqualTo(3);
        assertThat(request.getPaymentMethodId()).isEqualTo("visa");
        assertThat(request.getToken()).isEqualTo("tok_abc");
        assertThat(request.getTransactionAmount()).isEqualByComparingTo("100.00");
        assertThat(request.getDescription()).isEqualTo("Pedido #42");
        assertThat(request.getExternalReference()).isEqualTo("42");
        assertThat(request.getPayer().getEmail()).isEqualTo("cliente@email.com");
        assertThat(request.getPayer().getIdentification().getType()).isEqualTo("CPF");
    }

    @Test
    void creditoStrategyDeveIdentificarCnpjQuandoDocumentoTemMaisDeOnzeDigitos() {
        DadosPagamentoDto dados = dados(TipoPagamento.CREDITO, BandeiraCartao.ELO, 1, "12345678000199");

        PaymentCreateRequest request = new CreditoStrategy().criar(dados);

        assertThat(request.getPayer().getIdentification().getType()).isEqualTo("CNPJ");
        assertThat(request.getPayer().getIdentification().getNumber()).isEqualTo("12345678000199");
        assertThat(request.getPaymentMethodId()).isEqualTo("elo");
    }

    @Test
    void debitoStrategyDeveForcarUmaUnicaParcela() {
        DadosPagamentoDto dados = dados(TipoPagamento.DEBITO, BandeiraCartao.MASTER, 6, "12345678901");

        PaymentCreateRequest request = new DebitoStrategy().criar(dados);

        assertThat(request.getInstallments()).isEqualTo(1);
        assertThat(request.getPaymentMethodId()).isEqualTo("master");
    }

    @Test
    void pixStrategyDeveUsarMetodoPixESemToken() {
        DadosPagamentoDto dados = dados(TipoPagamento.PIX, null, null, "12345678901");

        PaymentCreateRequest request = new PixStrategy().criar(dados);

        assertThat(request.getPaymentMethodId()).isEqualTo("pix");
        assertThat(request.getToken()).isNull();
        assertThat(request.getInstallments()).isNull();
        assertThat(request.getDescription()).isEqualTo("Pedido #42");
    }
}
