package com.br.ecommerce.loja.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "loja")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loja {

    @Id
    private String id;
    private String corPrimaria;
}
