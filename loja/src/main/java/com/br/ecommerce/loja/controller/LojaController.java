package com.br.ecommerce.loja.controller;

import com.br.ecommerce.loja.dto.LojaRequest;
import com.br.ecommerce.loja.dto.LojaResponse;
import com.br.ecommerce.loja.service.LojaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loja")
@RequiredArgsConstructor
public class LojaController {

    private final LojaService lojaService;

    @GetMapping
    public ResponseEntity<LojaResponse> buscaConfig(){
        LojaResponse lojaResponse = lojaService.buscaConfig();
        return ResponseEntity.ok(lojaResponse);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LojaResponse> alteraConfig(@ModelAttribute LojaRequest lojaRequest){
        LojaResponse lojaResponse = lojaService.alteraPersonalizacao(lojaRequest);
        return ResponseEntity.ok(lojaResponse);
    }

}
