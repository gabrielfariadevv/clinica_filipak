package com.a3.clinica.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequest {
    private String name;
    private String cpf;
    private String telefone;
    private Long userId;
}
