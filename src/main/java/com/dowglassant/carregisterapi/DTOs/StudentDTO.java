package com.dowglassant.carregisterapi.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private String nomeCompleto;

    private String matricula;

    private String serie;

    private String idade;
}
