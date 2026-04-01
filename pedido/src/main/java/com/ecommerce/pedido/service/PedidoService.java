package com.ecommerce.pedido.service;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.dto.PedidoRequest;
import com.ecommerce.pedido.dto.PedidoResponse;
import com.ecommerce.pedido.mapper.PedidoMapper;
import com.ecommerce.pedido.model.Pedido;
import com.ecommerce.pedido.repository.PedidoRepository;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoMapper pedidoMapper;
    private final PedidoRepository pedidoRepository;
    private final PagamentoService pagamentoService;

    public PedidoResponse criaPedido(PedidoRequest pedidoRequest){
        Pedido pedido = pedidoMapper.map(pedidoRequest);
        pedidoRepository.save(pedido);

        DadosPagamentoDto dadosPagamento = new DadosPagamentoDto(
                pedido.getId(),
                pedido.getValorTotal(),
                pedidoRequest.pagamento().tokenCartao(),
                pedido.getPagamentoPedido().getTipoPagamento(),
                pedido.getPagamentoPedido().getBandeiraCartao(),
                pedido.getPagamentoPedido().getParcelas(),
                pedidoRequest.pagamento().emailPagador(),
                pedidoRequest.pagamento().cpfCnpjPagador(),
                pedidoRequest.pagamento().primeiroNomePagador(),
                pedidoRequest.pagamento().segundoNomePagador()
        );

        pagamentoService.geraPagamento(dadosPagamento);

    }

}
