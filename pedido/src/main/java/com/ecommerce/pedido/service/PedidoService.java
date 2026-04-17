package com.ecommerce.pedido.service;

import com.ecommerce.pedido.dto.*;
import com.ecommerce.pedido.enums.StatusPagamento;
import com.ecommerce.pedido.mapper.PedidoMapper;
import com.ecommerce.pedido.model.Pedido;
import com.ecommerce.pedido.publisher.ProdutoPublisher;
import com.ecommerce.pedido.publisher.representation.ProdutoRepresentation;
import com.ecommerce.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoMapper pedidoMapper;
    private final PedidoRepository pedidoRepository;
    private final PagamentoService pagamentoService;
    private final ProdutoPublisher produtoPublisher;
    private final ProdutoValidacaoService produtoValidacaoService;
    private final ParticipanteValidacaoService participanteValidacaoService;

    public PedidoCriadoResponse criaPedido(PedidoRequest pedidoRequest){

        validacoes(pedidoRequest);
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

        PagamentoResponse pagamentoResponse = pagamentoService.geraPagamento(dadosPagamento);

        if(StatusPagamento.APROVADO.equals(pagamentoResponse.statusPagamento())) {
            publicaProdutosVendidos(pedidoRequest.itensPedido());
        }

        return new PedidoCriadoResponse(pedido.getId(), pagamentoResponse.valorPagamento(), pagamentoResponse.statusPagamento(),
                pagamentoResponse.qrCodePix(), pagamentoResponse.qrCodeBase64Pix());
    }

    private void publicaProdutosVendidos(List<ItemPedidoRequest> itemPedidos){
        List<ProdutoRepresentation> produtos = itemPedidos.stream().map(itemPedido -> new ProdutoRepresentation(itemPedido.idProduto(), itemPedido.idProdutoVariacao(), itemPedido.quantidade())).toList();
        produtoPublisher.publicar(produtos);
    }

    private void validacoes(PedidoRequest pedidoRequest){
        produtoValidacaoService.validar(pedidoRequest.itensPedido());
        participanteValidacaoService.validar(pedidoRequest.idParticipante());
    }
}
