package com.dowglassant.carregisterapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    private String nomeCompleto;

    private String cpf;

    private String matricula;

    private String dataDeNascimento;

    private String nomeDaMae;

    private String enderecoRua;

    private String enderecoBairro;

    private String enderecoCidade;

    private String enderecoEstado;

    private String enderecoNumero;

    private Integer idade;

    private String serie;

    private String contatoDoResponsavel;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;
}
