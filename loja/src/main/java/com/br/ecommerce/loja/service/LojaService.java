package com.br.ecommerce.loja.service;

import com.br.ecommerce.loja.dto.BucketFile;
import com.br.ecommerce.loja.dto.LojaRequest;
import com.br.ecommerce.loja.dto.LojaResponse;
import com.br.ecommerce.loja.exception.exception.CorInvalidaException;
import com.br.ecommerce.loja.exception.exception.LogoInvalidaException;
import com.br.ecommerce.loja.model.Loja;
import com.br.ecommerce.loja.repository.LojaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LojaService {

    private static final String ID_DEFAULT = "default-config";
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([0-9A-Fa-f]{3}|[0-9A-Fa-f]{4}|[0-9A-Fa-f]{6}|[0-9A-Fa-f]{8})$");

    private final LojaRepository lojaRepository;
    private final BucketService bucketService;

    public LojaResponse alteraPersonalizacao(LojaRequest lojaRequest){
        if(!verificaSeArquivoEImagem(lojaRequest.logo())) throw new LogoInvalidaException("logo", "Arquivo da logo deve ser uma imagem");
        if(!codigoHexadecimal(lojaRequest.corPrimaria())) throw new CorInvalidaException("corPrimaria", "Cor não está no modelo hexadecimal!");

        try{
            bucketService.delete(ID_DEFAULT);
            bucketService.upload(new BucketFile(bucketService.retornaNomeLogo(ID_DEFAULT),
                    lojaRequest.logo().getInputStream(),
                    lojaRequest.logo().getContentType(),
                    lojaRequest.logo().getSize()));
        } catch (Exception e) {
            throw new LogoInvalidaException("logo", "Arquivo da logo inválido!");
        }

        Loja loja = new Loja(ID_DEFAULT, lojaRequest.corPrimaria());
        lojaRepository.save(loja);

        return new LojaResponse(loja.getCorPrimaria(), bucketService.getUrl(ID_DEFAULT));
    }

    public LojaResponse buscaConfig(){
        Loja loja = lojaRepository.findByid(ID_DEFAULT);
        return new LojaResponse(loja.getCorPrimaria(), bucketService.getUrl(ID_DEFAULT));
    }

    private boolean verificaSeArquivoEImagem(MultipartFile arquivo){
        if (arquivo != null && arquivo.getContentType().startsWith("image/")) {
            return true;
        }

        return false;
    }

    private boolean codigoHexadecimal(String value) { return value != null && HEX_COLOR_PATTERN.matcher(value).matches(); }
}
