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

public class Recharge {
    private String cellPhone;
    private Double value;
    private String supplierId;
}
