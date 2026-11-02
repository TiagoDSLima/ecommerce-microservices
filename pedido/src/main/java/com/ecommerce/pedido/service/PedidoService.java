package com.ecommerce.pedido.service;

import com.ecommerce.pedido.client.representation.ProdutoRepresentation;
import com.ecommerce.pedido.dto.*;
import com.ecommerce.pedido.mapper.PedidoMapper;
import com.ecommerce.pedido.model.Pedido;
import com.ecommerce.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoMapper pedidoMapper;
    private final PedidoRepository pedidoRepository;
    private final PagamentoService pagamentoService;
    private final ProdutoValidacaoService produtoValidacaoService;
    private final ParticipanteValidacaoService participanteValidacaoService;

    // Se o Mercado Pago falhar, o pedido não fica gravado sem pagamento
    @Transactional
    public PedidoCriadoResponse criaPedido(PedidoRequest pedidoRequest, Long idUsuario){

        Map<Long, ProdutoRepresentation> produtos = produtoValidacaoService.validar(pedidoRequest.itensPedido());
        Long idParticipante = participanteValidacaoService.buscaIdParticipante(idUsuario);
        Pedido pedido = pedidoMapper.map(comPrecosDoCatalogo(pedidoRequest, produtos));
        pedido.setIdParticipante(idParticipante);
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

        // Se aprovado na hora, o PagamentoService já publica os produtos vendidos
        PagamentoResponse pagamentoResponse = pagamentoService.geraPagamento(dadosPagamento);

        return new PedidoCriadoResponse(pedido.getId(), pagamentoResponse.valorPagamento(), pagamentoResponse.statusPagamento(),
                pagamentoResponse.qrCodePix(), pagamentoResponse.qrCodeBase64Pix());
    }

    // Preço e descrição vêm do serviço de Produto; os valores enviados pelo cliente são descartados
    private PedidoRequest comPrecosDoCatalogo(PedidoRequest pedidoRequest, Map<Long, ProdutoRepresentation> produtos){
        List<ItemPedidoRequest> itens = pedidoRequest.itensPedido().stream()
                .map(item -> {
                    ProdutoRepresentation produto = produtos.get(item.idProduto());
                    return new ItemPedidoRequest(produto.descricao(), produto.valorUnitario(), item.quantidade(),
                            item.idProduto(), item.idProdutoVariacao());
                })
                .toList();

        return new PedidoRequest(itens, pedidoRequest.endereco(), pedidoRequest.pagamento());
    }
}
