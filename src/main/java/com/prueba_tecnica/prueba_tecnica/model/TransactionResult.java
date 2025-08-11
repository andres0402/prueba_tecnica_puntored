package com.prueba_tecnica.prueba_tecnica.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class TransactionResult {
    private String message;
    private String transactionalID;
    private String cellPhone;
    private String value;
}
