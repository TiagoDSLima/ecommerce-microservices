package com.br.ecommerce.loja.dto;

import org.springframework.web.multipart.MultipartFile;

public record LojaRequest(String corPrimaria, MultipartFile logo) {
}
