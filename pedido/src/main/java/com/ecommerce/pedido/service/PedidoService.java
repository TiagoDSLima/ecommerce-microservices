package com.ecommerce.pedido.service;

import com.ecommerce.pedido.mapper.PedidoMapper;
import com.ecommerce.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoMapper pedidoMapper;
    private final PedidoRepository pedidoRepository;



}
