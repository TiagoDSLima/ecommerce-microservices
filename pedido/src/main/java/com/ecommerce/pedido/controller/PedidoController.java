package com.ecommerce.pedido.controller;

import com.ecommerce.pedido.dto.PedidoCriadoResponse;
import com.ecommerce.pedido.dto.PedidoRequest;
import com.ecommerce.pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoCriadoResponse> criarPedido(@RequestBody PedidoRequest pedidoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criaPedido(pedidoRequest));
    }
}
