package com.a3.clinica.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {
    private Long id;
    private String name;
    private String cpf;
    private String telefone;
    private String username;
    private String role;

}
