package com.prueba_tecnica.prueba_tecnica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class TransactionResult {
    @Column(name = "transactionalID")
    @Id
    private String transactionalID;

    @Column(name = "message")
    private String message;

    @Column(name = "cellPhone")
    private String cellPhone;

    @Column(name = "value")
    private String value;
}
