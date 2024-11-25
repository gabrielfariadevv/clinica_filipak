package com.a3.clinica.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDto {

    private String fileName;
    private String contentType;
    private String fileContent;
}

